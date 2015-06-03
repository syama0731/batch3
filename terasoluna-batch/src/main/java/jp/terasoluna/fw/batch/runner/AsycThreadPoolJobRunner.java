package jp.terasoluna.fw.batch.runner;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.constant.BatchJobStatusConstant;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import jp.terasoluna.fw.batch.launcher.JobLauncher;

public class AsycThreadPoolJobRunner implements JobRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(AsycThreadPoolJobRunner.class);
    
    @Inject
    SystemDao systemDao;
    
    @Inject
    BLogicParam blogicParam;
    
    @Inject
    String terminalFileName;
    
    @Inject
    JobLauncher jobLauncher;
    
    @Override
    public Integer runJob(String[] args) {
        Integer result = 0;
        CmdLineParser cmdLineParser = new CmdLineParser(blogicParam);
        
        // コマンドライン引数をパースし、blogicParamにマッピング
        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            logger.error("cmd line parse error", e);
        }
        
        BatchJobManagementParam batchJobManagementParam = new BatchJobManagementParam();
        batchJobManagementParam.setJobSequenceId(blogicParam.getJobSequenceId());
        batchJobManagementParam.setForUpdate(false);
        
        BLogicParam param = null;
        BatchJobManagementUpdateParam batchJobManagementUpdateParam = null;
        
        // 終了ファイルが置かれるまでループ
        while(!checkTerminalFile(terminalFileName)) {
            param = systemDao.selectJob(batchJobManagementParam);
            
            batchJobManagementUpdateParam = new BatchJobManagementUpdateParam();
            batchJobManagementUpdateParam.setJobSequenceId(param.getJobSequenceId());
            batchJobManagementUpdateParam.setCurAppStatus(BatchJobStatusConstant.EXECUTING_STATUS);
            
            if(systemDao.updateJobTable(batchJobManagementUpdateParam) != 0) {
                // ジョブを実行する
                jobLauncher.launchJob(param.getJobAppCd(), param);
            }
            
        }
        
        // ジョブランチャーで実行中のジョブの終了を待つ
        while(jobLauncher.isFinished());
        
        return result;
    }
    
    protected boolean checkTerminalFile(String fileName) {
        return false;
        // TODO implement
    }

}
