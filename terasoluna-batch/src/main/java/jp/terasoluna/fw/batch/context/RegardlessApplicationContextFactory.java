package jp.terasoluna.fw.batch.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RegardlessApplicationContextFactory extends
        AbstractApplicationContextFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(RegardlessApplicationContextFactory.class);
    
    @Override
    public ApplicationContext getApplicationContext(String... batchBeanFileName) {
        ApplicationContext ctx = null;
        
        try {
            // コンテキストのインスタンス生成
            ctx = new ClassPathXmlApplicationContext(batchBeanFileName, getParent());
        } catch (Exception e) {
            logger.error("error in load bean definition :", e);
            throw e;
        }

        return ctx;
    }
    
    @Override
    public void close() {
        // nothing to do
    }

}
