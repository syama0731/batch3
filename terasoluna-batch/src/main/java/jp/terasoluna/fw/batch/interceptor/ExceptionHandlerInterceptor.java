package jp.terasoluna.fw.batch.interceptor;

import javax.inject.Inject;
import javax.inject.Named;

import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ExceptionHandlerInterceptor implements MethodInterceptor {

    @Inject
    @Named("defaultExceptionHandler")
    ExceptionHandler defaultExceptionHandler;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        Object result = null;
        
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            if(defaultExceptionHandler != null) {
                result = defaultExceptionHandler.handleThrowableException(e);
            }
        }
        
        return result;
    }

}
