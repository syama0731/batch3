package jp.terasoluna.fw.batch.launcher;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;

public interface JobLauncher {

    Integer launchJob(String jobName, BLogicParam param);
    
    void close();
    
    boolean isFinished();
    
}
