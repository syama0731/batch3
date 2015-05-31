package jp.terasoluna.fw.batch.interceptor;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);
    
    @Inject
    protected int retryCount = 0;
    
    @Inject
    protected long waitTime = 0L;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        for(int i = 0;i <= retryCount; i++) {
            try {
                return invocation.proceed();
            } catch (Exception e) {
                if(logger.isWarnEnabled()) {
                    logger.warn("retry in {} times with {} milliseconds, because of {}", i+1, waitTime, e.getMessage());
                }
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    logger.warn("InterruptedException occured :", ie);
                }
            }
        }
        
        if(logger.isWarnEnabled()) {
            logger.warn("incomplete finish, because of reaching retry count max {} times", retryCount);
        }
        
        return null;
    }

}
