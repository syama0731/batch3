package jp.terasoluna.fw.batch.async.controller;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("taskExecutorDelegate")
public class TaskExecutorDelegatorImpl implements TaskExecutorDelegate {

    @Resource
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void execute(Runnable task) {
        threadPoolTaskExecutor.execute(task);
    }

    @Override
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }
}
