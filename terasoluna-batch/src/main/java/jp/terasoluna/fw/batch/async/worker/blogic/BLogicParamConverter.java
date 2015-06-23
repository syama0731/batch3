package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

public interface BLogicParamConverter {
    BLogicParam convertBLogicParam(BatchJobData batchJobData);
}
