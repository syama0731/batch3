package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import org.springframework.context.ApplicationContext;

public interface BLogicApplicationContextResolver {
    ApplicationContext resolveApplicationContext(BatchJobData batchJobData);
    void closeApplicationContext(ApplicationContext bLogicApplicationContext);
}
