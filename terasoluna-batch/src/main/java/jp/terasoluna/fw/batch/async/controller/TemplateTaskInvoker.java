package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("taskInvoker")
public class TemplateTaskInvoker implements TaskInvoker {

    @Resource
    protected JobExecutorTemplate jobExecutorTemplate;

    @Override
    public void invokeTask(String jobSequenceId) {
        jobExecutorTemplate.executeWorker(jobSequenceId);
    }
}
