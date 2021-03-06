/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DaoCollectorTest
 */
public class DaoCollectorFinalize001Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DaoCollectorFinalize001Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    private int previousThreadCount = 0;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryResultHandleDao(UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        DaoCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DaoCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
    }

    @Override
    protected void onSetUp() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        super.onSetUp();
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
    }

    @Override
    protected void onTearDown() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        CollectorTestUtil.allInterrupt();
        super.onTearDown();
    }

    /**
     * {@link DaoCollector#finalize()}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorFinalize001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        {
            Collector<UserBean> col = new DaoCollector<UserBean>(
                    this.userListQueryResultHandleDao, "collect", null);
            try {
                for (UserBean user : col) {
                    // あえて途中で抜ける
                    break;
                }
            } catch (Throwable e) {
                throw new SystemException(e);
            } finally {
                // あえてクローズせずに放置
                // DaoCollector.closeQuietly(it);
            }
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));

        System.gc();
        System.gc();
        System.gc();
    }

}
