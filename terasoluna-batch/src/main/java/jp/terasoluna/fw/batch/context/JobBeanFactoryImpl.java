package jp.terasoluna.fw.batch.context;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.context.util.BeanDefinitionFileFinder;

public class JobBeanFactoryImpl implements JobBeanFactory {
    
    @Inject
    private ApplicationContextFactory blogicFactory;
    
    @Inject
    private BeanDefinitionFileFinder finder;

    @Override
    public BLogic getBLogic(String name) {
        
        String filePath = finder.getFilePath(name);
        
        ApplicationContext ctx = blogicFactory.getApplicationContext(filePath);
        
        return ctx.getBean(name, BLogic.class);
    }

}
