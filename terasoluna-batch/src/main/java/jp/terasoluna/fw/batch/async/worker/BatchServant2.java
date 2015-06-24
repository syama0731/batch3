package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

public interface BatchServant2 {
    BLogicResult execute(BatchJobData batchJobData);
}
