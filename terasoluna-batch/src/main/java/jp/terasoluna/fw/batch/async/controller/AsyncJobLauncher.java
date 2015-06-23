package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;

public interface AsyncJobLauncher {

    void executeTask(BatchJobListResult batchJobListResult);

    void shutdown();
}
