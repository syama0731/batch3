package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.executor.concurrent.BatchServant;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BatchServantTaskInvoker implements TaskInvoker, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    protected String batchServantName = "batchServant";

    @Override
    public void invokeTask(String jobSequenceId) {
        BatchServant batchServant = applicationContext.getBean(batchServantName, BatchServant.class);
        batchServant.setJobSequenceId(jobSequenceId);
        batchServant.run();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBatchServantName(String batchServantName) {
        this.batchServantName = batchServantName;
    }
}
