package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("bLogicExecutor")
public class BLogicExecutorImpl implements BLogicExecutor {

    @Override
    public BLogicResult execute(ApplicationContext bLogicApplicationContext, BLogic bLogic, BLogicParam bLogicParam) {
        BLogicResult result = new BLogicResult();
        int status = bLogic.execute(bLogicParam);
        result.setBlogicStatus(status);
        return result;
    }
}
