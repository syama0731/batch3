package jp.terasoluna.fw.batch.async.controller;

public interface TaskInvoker {
    void invokeTask(String jobSequenceId);
}
