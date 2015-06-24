package jp.terasoluna.fw.batch.async.worker;

import jp.terasoluna.fw.batch.async.worker.blogic.BLogicApplicationContextResolver;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicParamConverter;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("batchServant2")
public class BatchServant2Impl implements BatchServant2 {

    @Resource
    protected BLogicParamConverter bLogicParamConverter;

    @Resource
    protected BLogicResolver bLogicResolver;

    @Resource
    protected BLogicExceptionHandlerResolver bLogicExceptionHandlerResolver;

    @Resource
    private BLogicApplicationContextResolver bLogicApplicationContextResolver;

    @Override
    public BLogicResult execute(final BatchJobData batchJobData) {
        ApplicationContext bLogicAppContext = bLogicApplicationContextResolver.resolveApplicationContext(batchJobData);
        BLogic blogic = bLogicResolver.resolveBLogic(bLogicAppContext, batchJobData);
        BLogicParam bLogicParam = bLogicParamConverter.convertBLogicParam(batchJobData);
        ExceptionHandler handler = bLogicExceptionHandlerResolver.resolveExceptionHandler(
                bLogicAppContext, batchJobData.getJobAppCd());
        BLogicResult result = new BLogicResult();
        try {
            result.setBlogicStatus(blogic.execute(bLogicParam));
        } catch (Throwable e) {
            if (handler != null) {
                result.setBlogicStatus(handler.handleThrowableException(e));
            }
            result.setBlogicThrowable(e);
        } finally {
            closeApplicationContext(bLogicAppContext);
        }
        return result;
    }

    protected void closeApplicationContext(ApplicationContext bLogicApplicationContext) {
        bLogicApplicationContextResolver.closeApplicationContext(bLogicApplicationContext);
    }
}

