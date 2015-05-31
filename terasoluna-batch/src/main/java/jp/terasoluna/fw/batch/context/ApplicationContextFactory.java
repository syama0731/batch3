package jp.terasoluna.fw.batch.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public interface ApplicationContextFactory {
    
    ApplicationContext getApplicationContext(String... batchBeanFileName);
    
    void closeContext(ApplicationContext context);
    
    void setParent(ApplicationContext parent);

    ApplicationContext getParent();
    
    void close();

}
