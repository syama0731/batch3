package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import org.springframework.stereotype.Component;

@Component("bLogicExecutor")
public class BLogicExecutorImpl implements BLogicExecutor {

    @Override
    public BLogicResult execute(BLogic bLogic, BLogicParam bLogicParam, ExceptionHandler handler) {
        BLogicResult result = new BLogicResult();
        try {
            int status = bLogic.execute(bLogicParam);
            result.setBlogicStatus(status);
        } catch (Throwable t) {
            if (handler != null) {
                result.setBlogicStatus(handler.handleThrowableException(t));
            }
            result.setBlogicThrowable(t);
        }
        return result;
    }
}
