package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

public interface BLogicExecutor {
    BLogicResult execute(BLogic bLogic, BLogicParam bLogicParam, ExceptionHandler handler);
}
