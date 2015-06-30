package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

public interface JobExecutorTemplate {
    boolean beforeExecute(String jobSequenceId);
    BLogicResult executeBLogic(String jobSequenceId);
    void afterExecute(String jobSequenceId, BLogicResult bLogicResult);
}
