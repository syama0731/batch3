package jp.terasoluna.fw.batch.executor.constant;

import lombok.Data;

@Data
public class BatchJobStatusConstant {

    public static final String NON_EXECUTED_STATUS = "0";
    
    public static final String EXECUTING_STATUS = "1";
    
    public static final String FINISHED_STATUS = "2";
}
