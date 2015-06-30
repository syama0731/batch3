package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.async.repository.BatchJobDataResolver;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

@Component("asyncJobOperator")
public class AsyncJobOperatorImpl implements AsyncJobOperator {

    private static final TLogger LOGGER = TLogger.getLogger(AsyncJobOperatorImpl.class);

    @Value("${polling.interval:1000}")
    protected long jobIntervalTime;

    @Resource
    protected BatchJobDataResolver batchJobDataResolver;

    @Resource
    protected AsyncJobLauncher asyncJobLauncher;

    @Resource
    protected AsyncBatchStopper asyncBatchStopper;

    @Resource
    protected ExceptionStatusHandler exceptionStatusHandler;

    @Override
    public int start(String[] args) {
        try {
            while (!asyncBatchStopper.canStop()) { // ポーリングループの停止条件
                // ジョブ管理テーブルの検索
                BatchJobListResult batchJobListResult = batchJobDataResolver.resolveBatchJobResult(args);
                if (batchJobListResult == null) {
                    TimeUnit.MILLISECONDS.sleep(jobIntervalTime);
                    continue;
                }
                // ジョブの実行
                asyncJobLauncher.executeTask(batchJobListResult.getJobSequenceId());
            }
        } catch (Exception e) {
            // ループ中断例外と返却ステータスコードの判定(TODO AOPにしてもいいが、Optionalな機能ではないため、ここで実装してよいか？)
            return exceptionStatusHandler.handleException(e);
        } finally {
            asyncJobLauncher.shutdown();
        }
        return 0;
    }
}

