package jp.terasoluna.fw.batch.async.controller;

public interface AsyncJobLauncher {

    void executeTask(String jobSequenceId);

    void shutdown();
}
