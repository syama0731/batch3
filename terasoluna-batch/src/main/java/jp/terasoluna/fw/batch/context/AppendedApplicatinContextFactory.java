package jp.terasoluna.fw.batch.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppendedApplicatinContextFactory extends
        AbstractApplicationContextFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(AppendedApplicatinContextFactory.class);
    
    private Set<String> includedBeanFileNameSet = Collections.synchronizedSet(new HashSet<String>()); 

    
    @Override
    public ApplicationContext getApplicationContext(String... batchBeanFileName) {
        
        Set<String> createBeanFileNameSet = new HashSet<String>();
        
        synchronized (this) {
            
            for(String fileName : batchBeanFileName) {
               if(includedBeanFileNameSet.contains(fileName)) {
                   createBeanFileNameSet.add(fileName);
                   includedBeanFileNameSet.add(fileName);
               }
            }
            
            if(!createBeanFileNameSet.isEmpty()) {
                try {
                    setParent(new ClassPathXmlApplicationContext((String[]) batchBeanFileName, getParent()));
                    return getParent();
                } catch (Exception e) {
                    logger.error("error in load bean definition :", e);
                    throw e;
                }
            }
        }
        
        return null;
    }

}
