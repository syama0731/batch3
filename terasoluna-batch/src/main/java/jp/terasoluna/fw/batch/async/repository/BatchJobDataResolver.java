package jp.terasoluna.fw.batch.async.repository;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;

public interface BatchJobDataResolver {
    // TODO 一気にBatchJobDataとっちゃえばいいのに・・・
    BatchJobListResult resolveBatchJobResult(String[] args);
    BatchJobData resolveBatchJobData(BatchJobManagementParam batchJobManagementParam);
}
