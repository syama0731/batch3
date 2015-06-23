package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import org.springframework.context.ApplicationContext;

public interface BLogicResolver {
    BLogic resolveBLogic(ApplicationContext ctx, BatchJobData batchJobData);
}
