package jp.terasoluna.fw.batch.async.controller;

public interface JobExecutorTemplate {
    boolean beforeExecute(String jobSequenceId);
    void executeWorker(String jobSequenceId);
}
