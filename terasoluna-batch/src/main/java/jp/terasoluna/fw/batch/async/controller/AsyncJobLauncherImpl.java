package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component("asyncJobLauncher")
public class AsyncJobLauncherImpl implements AsyncJobLauncher, InitializingBean {

    private static final TLogger LOGGER = TLogger.getLogger(AsyncJobLauncherImpl.class);

    @Resource
    protected TaskExecutorDelegate taskExecutorDelegate;

    @Resource
    protected JobExecutorTemplate jobExecutorTemplate;

    /**
     * スレッドプールの上限以上のタスク流入を防ぐためのセマフォ
     */
    protected Semaphore taskPoolLimit = null;

    @Value("${executor.jobTerminateWaitInterval}")
    protected volatile long executorJobTerminateWaitIntervalTime = 5000L;

    @Value("${batchTaskExecutor.maxPoolSize:-1}")
    protected int semaphoreSize;

    protected boolean fair = true;

    @Override
    public void executeTask(final String jobSequenceId) {
        Assert.notNull(jobSequenceId);
        try {
            taskPoolLimit.acquire();
            if (!jobExecutorTemplate.beforeExecute(jobSequenceId)) {
                taskPoolLimit.release();
                return;
            }
            taskExecutorDelegate.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        launchJob(jobSequenceId);
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

    protected void launchJob(String jobSequenceId) {
        BLogicResult result = null;
        try {
            result = jobExecutorTemplate.executeBLogic(jobSequenceId);
        } finally {
            jobExecutorTemplate.afterExecute(jobSequenceId, result);
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

    public void setFair(boolean fair) {
        this.fair = fair;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(semaphoreSize > 0, "semaphore size must be defined at ${batchTaskExecutor.maxPoolSize} in property file.");
        taskPoolLimit = new Semaphore(semaphoreSize);
    }
}
