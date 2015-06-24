package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.async.repository.BatchJobDataResolver;
import jp.terasoluna.fw.batch.async.worker.BatchServant2;
import jp.terasoluna.fw.batch.async.repository.BatchStatusChanger;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Controller("asyncJobLauncher")
public class AsyncJobLauncherImpl implements AsyncJobLauncher , InitializingBean {

    private static final TLogger LOGGER = TLogger.getLogger(AsyncJobLauncherImpl.class);

    @Resource
    private BatchStatusChanger batchStatusChanger;

    @Resource
    private BatchJobDataResolver batchJobDataResolver;

    @Resource
    private BatchServant2 batchServant2;

    @Resource
    protected TaskExecutorDelegate taskExecutorDelegate;

    /**
     * スレッドプールの上限以上のタスク流入を防ぐためのセマフォ
     */
    protected Semaphore taskPoolLimit = null;

    private volatile long executorJobTerminateWaitIntervalTime = 5000L;

    protected boolean fair = true;

    @Override
    public void executeTask(final BatchJobListResult batchJobListResult) {
        try {
            taskPoolLimit.acquire();
            final BatchJobData batchJobData = beforeExecute(batchJobListResult);
            if (batchJobData == null) {
                taskPoolLimit.release();
                return;
            }
            taskExecutorDelegate.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        executeServant(batchJobData);
                    } finally {
                        taskPoolLimit.release();
                    }
                }
            });
        } catch (TaskRejectedException e) {
            // TODO タスク実行前に拒否されたログを出す。
            taskPoolLimit.release();
        } catch (InterruptedException e) {
            // TODO セマフォ待ち受けで割り込み発生（未実行だがステータスは開始になっちゃったjobSequenceIdのログを出してポーリングループを中断させる必要あり）
            e.printStackTrace();
        }

    }

    // TODO 既にセマフォ獲得済みなので、ここでは例外を外部にスローさせない必要があるが実装が汚い。
    protected BatchJobData beforeExecute(BatchJobListResult batchJobListResult) {
        final String jobSequenceId = batchJobListResult.getJobSequenceId();
        try {
            // ジョブステータスを"開始"に変更
            boolean updated = batchStatusChanger.changeToStartStatus(jobSequenceId);
            if (!updated) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025010, batchJobListResult.getJobSequenceId());
                }
                return null;
            }
        } catch (RuntimeException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(LogId.IAL025010, batchJobListResult.getJobSequenceId(), e);
            }
            return null;
        }

        BatchJobManagementParam param = new BatchJobManagementParam();
        param.setJobSequenceId(jobSequenceId);
        param.setForUpdate(false);

        try {
            return batchJobDataResolver.resolveBatchJobData(param);
        } catch (RuntimeException e) {
            // TODO ログ出し。ジョブ管理テーブル再取得時のリトライオーバー(そもそも再取得は必要なの？）
            return null;
        }
    }

    protected void executeServant(BatchJobData batchJobData) {
        BLogicResult result = null;
        try {
            result = batchServant2.execute(batchJobData);
        } finally {
            try {
                // ジョブステータスを"終了"に変更
                boolean status = batchStatusChanger.changeToEndStatus(batchJobData.getJobSequenceId(), result);
                if (!status) {
                    if (LOGGER.isErrorEnabled()) {
                        if (result != null) { // TODO 汚いが必要になった・・・
                            LOGGER.error(LogId.EAL025025, batchJobData.getJobSequenceId(), result.getBlogicStatus());
                        } else {
                            LOGGER.error(LogId.EAL025025, batchJobData.getJobSequenceId(), null);
                        }
                    }
                }
            } catch (Exception e) {
                if (LOGGER.isErrorEnabled()) {
                    if (result != null) { // TODO 汚いが必要になった・・・
                        LOGGER.error(LogId.EAL025025, batchJobData.getJobSequenceId(), result.getBlogicStatus(), e);
                    } else {
                        LOGGER.error(LogId.EAL025025, batchJobData.getJobSequenceId(), null, e);
                    }
                }
            }
        }
    }

    @Override
    public void shutdown() {
        ThreadPoolTaskExecutor taskExecutor = taskExecutorDelegate.getThreadPoolTaskExecutor();
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.shutdown();
        while (taskExecutor.getActiveCount() != 0) {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(LogId.DAL025031, taskExecutor.getActiveCount());
                }
                TimeUnit.MILLISECONDS.sleep(executorJobTerminateWaitIntervalTime);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.taskPoolLimit = new Semaphore(taskExecutorDelegate.getThreadPoolTaskExecutor().getMaxPoolSize(), this.fair);
        String executorJobTerminateWaitIntervalTimeStr = PropertyUtil.getProperty("executor.jobTerminateWaitInterval");
        if (executorJobTerminateWaitIntervalTimeStr != null) {
            executorJobTerminateWaitIntervalTime = Long.parseLong(executorJobTerminateWaitIntervalTimeStr);
        }
    }

    public void setFair(boolean fair) {
        this.fair = fair;
    }
}
