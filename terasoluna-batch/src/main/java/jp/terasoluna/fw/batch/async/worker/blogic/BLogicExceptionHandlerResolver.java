package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import org.springframework.context.ApplicationContext;

public interface BLogicExceptionHandlerResolver {
    ExceptionHandler resolveExceptionHandler(ApplicationContext ctx, String jobAppCd);
}
