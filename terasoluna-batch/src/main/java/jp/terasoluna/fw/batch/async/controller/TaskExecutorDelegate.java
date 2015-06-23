package jp.terasoluna.fw.batch.async.controller;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadPoolTaskExecutorのインターセプタとして、CGLIB使用回避のために使用される。
 */
public interface TaskExecutorDelegate extends TaskExecutor {
    ThreadPoolTaskExecutor getThreadPoolTaskExecutor();
}
