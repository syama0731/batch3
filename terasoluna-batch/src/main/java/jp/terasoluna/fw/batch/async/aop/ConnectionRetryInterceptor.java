package jp.terasoluna.fw.batch.async.aop;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.RetryableExecuteException;
import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.transaction.TransactionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.TimeUnit;

public class ConnectionRetryInterceptor implements MethodInterceptor, InitializingBean {

    private static final TLogger LOGGER = TLogger.getLogger(ConnectionRetryInterceptor.class);

    /**
     * 最大リトライ回数
     */
    private volatile long maxRetryCount = 0;

    /**
     * データベース異常時のリトライ間隔（ミリ秒）
     */
    private volatile long retryInterval = 20000;

    /**
     * リトライ回数をリセットする、前回からの発生間隔のデフォルト値
     */
    private volatile long retryReset = 600000;

    /**
     * データベース異常時のリトライ回数定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_MAX = "batchTaskExecutor.dbAbnormalRetryMax";

    /**
     * データベース異常時のリトライ間隔定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_INTERVAL = "batchTaskExecutor.dbAbnormalRetryInterval";

    /**
     * データベース異常時のリトライ回数をリセットする前回からの発生間隔定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_RESET = "batchTaskExecutor.dbAbnormalRetryReset";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int retryCount = 0;
        Exception cause = null;
        Object returnObject = null;
        long lastExceptionTime = System.currentTimeMillis();
        while (true) {
            try {
                returnObject = invocation.proceed();
                break;
            } catch (DataAccessException | TransactionException | RetryableExecuteException e) {
                if (System.currentTimeMillis() - lastExceptionTime > retryReset) {
                    // TODO 前回例外発生から指定時間以上経過のため、リトライ回数リセット（ログ出し）
                    retryCount = 0;
                }
                lastExceptionTime = System.currentTimeMillis();

                if (e instanceof RetryableExecuteException) {
                    // RetryableExecuteExceptionの場合は原因例外をログ出し、リトライオーバー時のスロー対象にする。
                    cause = ((RetryableExecuteException) e.getCause());
                } else {
                    cause = e;
                }

                if (retryCount >= maxRetryCount) {
                    LOGGER.error(LogId.EAL025031, cause);
                    break;
                }

                TimeUnit.MILLISECONDS.sleep(retryInterval);
                retryCount++;
                LOGGER.info(LogId.IAL025017, retryCount, maxRetryCount, retryReset, retryInterval);
                // TODO トレースログ消したい。
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025010, BatchUtil.getMemoryInfo());
                }
            }
        }
        if (cause != null) {
            throw cause;
        }
        return returnObject;
    }

    public void setMaxRetryCount(long maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public void setRetryReset(long retryReset) {
        this.retryReset = retryReset;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String dbAbnormalRetryMaxStr = PropertyUtil.getProperty(BATCH_DB_ABNORMAL_RETRY_MAX);
        String dbAbnormalRetryIntervalStr = PropertyUtil.getProperty(BATCH_DB_ABNORMAL_RETRY_INTERVAL);
        String dbAbnormalRetryResetStr = PropertyUtil.getProperty(BATCH_DB_ABNORMAL_RETRY_RESET);

        if (dbAbnormalRetryMaxStr != null && dbAbnormalRetryMaxStr.length() > 0) {
            try {
                maxRetryCount = Long.parseLong(dbAbnormalRetryMaxStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_DB_ABNORMAL_RETRY_MAX, dbAbnormalRetryMaxStr);
                throw e;
            }
        }

        if (dbAbnormalRetryIntervalStr != null && dbAbnormalRetryIntervalStr.length() > 0) {
            try {
                retryInterval = Long.parseLong(dbAbnormalRetryIntervalStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_DB_ABNORMAL_RETRY_INTERVAL, dbAbnormalRetryIntervalStr);
                throw e;
            }
        }

        if (dbAbnormalRetryResetStr != null && dbAbnormalRetryResetStr.length() > 0) {
            try {
                retryReset = Long.parseLong(dbAbnormalRetryResetStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_DB_ABNORMAL_RETRY_RESET, dbAbnormalRetryResetStr);
                throw e;
            }
        }
    }
}
