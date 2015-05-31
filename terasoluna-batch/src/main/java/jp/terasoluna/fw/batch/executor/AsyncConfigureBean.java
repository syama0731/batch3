package jp.terasoluna.fw.batch.executor;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class AsyncConfigureBean {
    
    @Value("${batchTaskExecutor.default:#{null}}")
    private String batchTaskExecutorName;
    
    @Value("${batchTaskExecutor.batchServant:#{null}}")
    private String batchTaskServantName;

    @Value("${batchTaskExecutor.dbAbnormalRetryMax:0}")
    private long dbAbnormalRetryMax;
    
    @Value("${batchTaskExecutor.dbAbnormalRetryInterval:20000}")
    private long dbAbnormalRetryInterval;
    
    @Value("${batchTaskExecutor.dbAbnormalRetryReset:600000}")
    private long dbAbnormalRetryReset;
    
    @Value("${batchTaskExecutor.executeRetryInterval:1000}")
    private long executeRetryInterval;
    
    @Value("${batchTaskExecutor.executeRetryCountMax:10}")
    private long executeRetryCountMax;
    
    @Value("${batchTaskExecutor.availableThreadThresholdCount:1}")
    private long availableThreadThresholdCount;
    
    @Value("${batchTaskExecutor.availableThreadThresholdWait:100}")
    private long availableThreadThresholdWait;
    
}
