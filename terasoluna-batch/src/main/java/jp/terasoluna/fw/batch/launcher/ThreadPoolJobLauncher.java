package jp.terasoluna.fw.batch.launcher;

import javax.inject.Inject;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.context.JobBeanFactory;
import jp.terasoluna.fw.batch.executor.concurrent.BatchServant;
import jp.terasoluna.fw.batch.executor.concurrent.BatchServantImpl;

public class ThreadPoolJobLauncher implements JobLauncher {
    
    @Inject
    ThreadPoolTaskExecutor taskExecutor;
    
    @Inject
    JobBeanFactory jobBeanFactory;
    
    boolean closeEnable = false;

    @Override
    public Integer launchJob(String jobName, BLogicParam param) {
        
        // TODO クラスをロードする実装が必要
        BatchServant servant = new BatchServantImpl();
        
        BLogic blogic = jobBeanFactory.getBLogic(jobName);
        
        servant.setBLogic(blogic, param);
        
        taskExecutor.execute(servant);
        
        return null;
    }

    @Override
    public boolean isFinished() {
        if(closeEnable) {
            if(taskExecutor.getActiveCount() == 0) {
                taskExecutor.destroy();
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void close() {
        taskExecutor.shutdown();
        this.closeEnable = true;
    }

}
