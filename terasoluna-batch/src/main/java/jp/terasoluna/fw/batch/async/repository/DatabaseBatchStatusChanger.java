package jp.terasoluna.fw.batch.async.repository;

import jp.terasoluna.fw.batch.constants.EventConstants;
import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Component("batchStatusChanger")
public class DatabaseBatchStatusChanger implements BatchStatusChanger {

    private static final TLogger LOGGER = TLogger.getLogger(DatabaseBatchStatusChanger.class);

    @Autowired
    protected SystemDao systemDao;

    @Resource
    protected PlatformTransactionManager adminTransactionManager;

    @Override
    public boolean changeToStartStatus(String jobSequenceId) {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = adminTransactionManager.getTransaction(new DefaultTransactionDefinition());

            // ジョブデータ取得
            BatchJobManagementParam param = new BatchJobManagementParam();
            param.setJobSequenceId(jobSequenceId);
            param.setForUpdate(true);
            BatchJobData batchJobData = systemDao.selectJob(param);
            if (batchJobData == null) {
                LOGGER.error(LogId.EAL025026, jobSequenceId);
                return false;
            }

            // 開始前ステータス判定
            if (!JobStatusConstants.JOB_STATUS_UNEXECUTION.equals(batchJobData.getCurAppStatus())) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025004, jobSequenceId, batchJobData.getBLogicAppStatus(),
                            EventConstants.EVENT_STATUS_START, batchJobData.getCurAppStatus());
                }
                return false;
            }

            // ジョブステータス更新
            String nextStatus = JobStatusConstants.JOB_STATUS_EXECUTING;
            LOGGER.debug(LogId.DAL025023, jobSequenceId, nextStatus);
            BatchJobManagementUpdateParam updateParam = new BatchJobManagementUpdateParam();
            updateParam.setJobSequenceId(batchJobData.getJobSequenceId());
            updateParam.setBLogicAppStatus(batchJobData.getBLogicAppStatus());
            updateParam.setCurAppStatus(nextStatus);
            updateParam.setUpdDateTime(getCurrentTime());
            int count = systemDao.updateJobTable(updateParam);
            if (count != 1) {
                LOGGER.error(LogId.EAL025042);
                return false;
            }
            adminTransactionManager.commit(transactionStatus);
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                adminTransactionManager.rollback(transactionStatus);
            }
        }
        return true;
    }

    @Override
    public boolean changeToEndStatus(String jobSequenceId, BLogicResult bLogicResult) {
        TransactionStatus transactionStatus = null;

        try {
            transactionStatus = adminTransactionManager.getTransaction(new DefaultTransactionDefinition());

            // ジョブデータ取得
            BatchJobManagementParam param = new BatchJobManagementParam();
            param.setJobSequenceId(jobSequenceId);
            param.setForUpdate(true);
            BatchJobData batchJobData = systemDao.selectJob(param);
            if (batchJobData == null) {
                LOGGER.error(LogId.EAL025026, jobSequenceId);
                return false;
            }

            // 終了前ステータス判定
            if (!JobStatusConstants.JOB_STATUS_EXECUTING.equals(batchJobData.getCurAppStatus())) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025004, batchJobData.getJobSequenceId(), batchJobData.getBLogicAppStatus(),
                            EventConstants.EVENT_STATUS_NORMAL_TERMINATION, batchJobData.getCurAppStatus());
                }
                return false;
            }

            // ジョブステータス更新
            String nextStatus =  JobStatusConstants.JOB_STATUS_PROCESSED;
            LOGGER.debug(LogId.DAL025023, jobSequenceId, nextStatus);
            BatchJobManagementUpdateParam updateParam = new BatchJobManagementUpdateParam();
            updateParam.setJobSequenceId(batchJobData.getJobSequenceId());
            updateParam.setBLogicAppStatus(Integer.toString(bLogicResult.getBlogicStatus()));
            updateParam.setCurAppStatus(nextStatus);
            updateParam.setUpdDateTime(getCurrentTime());
            int count = systemDao.updateJobTable(updateParam);
            if (count != 1) {
                LOGGER.error(LogId.EAL025042);
                return false;
            }
            adminTransactionManager.commit(transactionStatus);
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                adminTransactionManager.rollback(transactionStatus);
            }
        }
        return true;
    }

    /**
     * <h6>カレント時刻を取得する.</h6>
     * TODO いい加減SQLへ移行させよう・・・
     * @return Timestamp カレント時刻
     */
    @Deprecated
    public Timestamp getCurrentTime() {
        Timestamp result = null;
        try {
            result = systemDao.readCurrentTime();
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025043, e);
            if (e instanceof DataAccessException) {
                throw (DataAccessException) e;
            }
        }
        return result;
    }
}
