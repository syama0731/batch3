package jp.terasoluna.fw.batch.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class StorableApplicatinContextFactoryImpl extends
        AbstractApplicationContextFactory implements StorableApplicationContextFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(StorableApplicatinContextFactoryImpl.class);
    
    protected GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
    
    public StorableApplicatinContextFactoryImpl() {
    }
    
    @Override
    public ApplicationContext getApplicationContext(String... batchBeanFileName) {
        ctx.load(batchBeanFileName);
        ctx.refresh();
        return ctx;
    }
    
    @Override
    public void setParent(ApplicationContext parent) {
        super.setParent(parent);
        ctx.setParent(parent);
    }

    @Override
    public void remove(String... beansFileName) {
        throw new NotImplementedException("sorry...");
    }
    
    @Override
    public void close() {
        
    }

}
