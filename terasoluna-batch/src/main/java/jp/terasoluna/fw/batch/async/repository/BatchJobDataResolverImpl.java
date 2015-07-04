package jp.terasoluna.fw.batch.async.repository;


import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("batchJobDataResolver")
public class BatchJobDataResolverImpl implements BatchJobDataResolver {

    @Autowired
    protected SystemDao systemDao;

    @Override
    public BatchJobListResult resolveBatchJobResult(String[] args) {
        String jobAppCd = null;
        if (args != null &&  args.length > 0) {
            jobAppCd = args[0];
        }
        BatchJobListParam param = new BatchJobListParam();
        param.setJobAppCd(jobAppCd);
        param.setCurAppStatusList(new ArrayList<String>(){{
            add(JobStatusConstants.JOB_STATUS_UNEXECUTION);
        }});
        List<BatchJobListResult> resultList = systemDao.selectJobList(param);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public BatchJobData resolveBatchJobData(BatchJobManagementParam batchJobManagementParam) {

        BatchJobData batchJobData = systemDao.selectJob(batchJobManagementParam);
        
        // 念のためトリムする
        if (batchJobData.getJobAppCd() != null) {
            batchJobData.setJobAppCd(batchJobData.getJobAppCd().trim());
        }
        
        return batchJobData;
    }
}
