package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.beans.Introspector;

@Component("bLogicExceptionHandlerResolver")
public class BLogicExceptionHandlerResolverImpl implements BLogicExceptionHandlerResolver {

    /**
     * 例外ハンドラのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX = "ExceptionHandler";

    /**
     * デフォルトの例外ハンドラのBean名.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME = "defaultExceptionHandler";

    @Override
    public ExceptionHandler resolveExceptionHandler(ApplicationContext ctx, String jobAppCd) {
        String exceptionHandlerBeanName = getExceptionHandlerBeanName(jobAppCd);
        if (ctx.containsBean(exceptionHandlerBeanName)) {
            return ctx.getBean(exceptionHandlerBeanName, ExceptionHandler.class);
        } else if (ctx.containsBean(Introspector.decapitalize(exceptionHandlerBeanName))) {
            return ctx.getBean(Introspector.decapitalize(exceptionHandlerBeanName), ExceptionHandler.class);
        }
        if (ctx.containsBean(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME)) {
            return ctx.getBean(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME, ExceptionHandler.class);
        }
        return null;
    }

    protected String getExceptionHandlerBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }
}
