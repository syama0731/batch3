package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.async.repository.BatchJobDataResolver;
import jp.terasoluna.fw.batch.async.repository.BatchStatusChanger;
import jp.terasoluna.fw.batch.async.worker.BLogicExecutor;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicApplicationContextResolver;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicParamConverter;
import jp.terasoluna.fw.batch.async.worker.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("jobExecutorTemplate")
public class JobExecutorTemplateImpl implements JobExecutorTemplate {

    private static final TLogger LOGGER = TLogger.getLogger(JobExecutorTemplateImpl.class);

    @Resource
    protected BLogicResolver bLogicResolver;

    @Resource
    protected BLogicExceptionHandlerResolver bLogicExceptionHandlerResolver;

    @Resource
    protected BLogicApplicationContextResolver bLogicApplicationContextResolver;

    @Resource
    protected BatchJobDataResolver batchJobDataResolver;

    @Resource
    protected BLogicParamConverter bLogicParamConverter;

    @Resource
    protected BLogicExecutor bLogicExecutor;

    @Resource
    protected BatchStatusChanger batchStatusChanger;

    @Override
    public boolean beforeExecute(String jobSequenceId) {
        try {
            // ジョブステータスを"開始"に変更
            boolean updated = batchStatusChanger.changeToStartStatus(jobSequenceId);
            if (!updated) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025010, jobSequenceId);
                }
                return false;
            }
        } catch (RuntimeException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(LogId.IAL025010, jobSequenceId, e);
            }
            return false;
        }
        return true;
    }

    @Override
    public BLogicResult executeBLogic(final String jobSequenceId) {
        BatchJobManagementParam param = new BatchJobManagementParam() {{
            setJobSequenceId(jobSequenceId);
            setForUpdate(false);
        }};
        BatchJobData batchJobData = batchJobDataResolver.resolveBatchJobData(param);

        ApplicationContext bLogicAppContext = bLogicApplicationContextResolver.resolveApplicationContext(batchJobData);
        BLogic bLogic = bLogicResolver.resolveBLogic(bLogicAppContext, batchJobData.getJobAppCd());
        BLogicParam bLogicParam = bLogicParamConverter.convertBLogicParam(batchJobData);
        ExceptionHandler handler = bLogicExceptionHandlerResolver.resolveExceptionHandler(
                bLogicAppContext, batchJobData.getJobAppCd());
        BLogicResult result = new BLogicResult();
        try {
            result = bLogicExecutor.execute(bLogic, bLogicParam);
        } catch (Throwable t) {
            result.setBlogicThrowable(t);
            if (handler != null) {
                result.setBlogicStatus(handler.handleThrowableException(t));
            }
        } finally {
            bLogicApplicationContextResolver.closeApplicationContext(bLogicAppContext);
        }
        return result;
    }

    protected void closeBLogicApplicationContext(ApplicationContext bLogicApplicationContext) {
        if (bLogicApplicationContext == null) {
            return;
        }
        if (bLogicApplicationContext instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) bLogicApplicationContext;
            aac.close();
            aac.destroy();
        }
    }

    @Override
    public void afterExecute(String jobSequenceId, BLogicResult result) {
        try {
            // ジョブステータスを"終了"に変更
            boolean status = batchStatusChanger.changeToEndStatus(jobSequenceId, result);
            if (!status) {
                if (LOGGER.isErrorEnabled()) {
                    if (result != null) { // TODO 汚いが必要になった・・・
                        LOGGER.error(LogId.EAL025025, jobSequenceId, result.getBlogicStatus());
                    } else {
                        LOGGER.error(LogId.EAL025025, jobSequenceId, null);
                    }
                }
            }
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                if (result != null) { // TODO 汚いが必要になった・・・
                    LOGGER.error(LogId.EAL025025, jobSequenceId, result.getBlogicStatus(), e);
                } else {
                    LOGGER.error(LogId.EAL025025, jobSequenceId, null, e);
                }
            }
        }
    }

}
