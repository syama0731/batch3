package jp.terasoluna.fw.batch.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractApplicationContextFactory implements
        ApplicationContextFactory {

    private ApplicationContext parent;
    
    @Override
    public void closeContext(ApplicationContext context) {
        if (context instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) context;
            aac.close();
            aac.destroy();
        }
    }
    
    @Override 
    public void setParent(ApplicationContext parent) {
        synchronized(parent) {
            this.parent = parent;
        }
    }
    
    @Override
    public ApplicationContext getParent() {
        return this.parent;
    }
    
    @Override
    public void close() {
        synchronized(parent) {
            if(parent instanceof ClassPathXmlApplicationContext) {
                ((ClassPathXmlApplicationContext) parent).close();
                ((ClassPathXmlApplicationContext) parent).destroy();
            }
        }
    }

}
