package jp.terasoluna.fw.batch.async.repository;

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

public interface BatchStatusChanger {
    boolean changeToStartStatus(String jobSequenceId);
    boolean changeToEndStatus(String jobSequenceId, BLogicResult bLogicResult);
}
