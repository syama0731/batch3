package jp.terasoluna.fw.batch.async.aop;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.RetryableExecuteException;
import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.logger.TLogger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.transaction.TransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.TimeUnit;

public class ConnectionRetryInterceptor implements MethodInterceptor {

    private static final TLogger LOGGER = TLogger.getLogger(ConnectionRetryInterceptor.class);

    /**
     * 最大リトライ回数
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryMax:0}")
    private volatile long maxRetryCount;

    /**
     * データベース異常時のリトライ間隔（ミリ秒）
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryInterval:20000}")
    private volatile long retryInterval;

    /**
     * リトライ回数をリセットする、前回からの発生間隔のデフォルト値
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryReset:600000}")
    private volatile long retryReset;

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
}
