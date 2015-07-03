package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import org.springframework.context.ApplicationContext;

public interface BLogicExecutor {
    BLogicResult execute(ApplicationContext bLogicApplicationContext, BLogic bLogic, BLogicParam bLogicParam);
}
