package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.async.controller.AsyncJobOperator;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AsyncBatchExecutor2 {

    private static final TLogger LOGGER = TLogger.getLogger(AsyncBatchExecutor2.class);

    /**
     * 管理用Bean定義ファイルを配置するクラスパス.
     */
    protected static final String BEAN_DEFINITION_ADMIN_CLASSPATH_KEY = "beanDefinition.admin.classpath";

    /**
     * 管理用Bean定義（基本部）
     */
    protected static final String BEAN_DEFINITION_DEFAULT = "beanDefinition.admin.default";

    /**
     * 管理用Bean定義（データソース部）
     */
    protected static final String BEAN_DEFINITION_DATASOURCE = "beanDefinition.admin.dataSource";

    /**
     * アプリケーションコンテキストクラス名.
     */
    protected static final String APPLICATION_CONTEXT = "org.springframework.context.support.ClassPathXmlApplicationContext";

    public static void main(String[] args) {
        AsyncBatchExecutor2 own = new AsyncBatchExecutor2();
        int status = own.execute(args);
        System.exit(status);
    }

    protected int execute(String[] args) {
        ApplicationContext ctx = null;
        int status = -1;
        try {
            ctx = getApplicationContext(getDefaultBeanFileName(), getDataSourceBeanFileName());
            if (ctx == null) {
                return status;
            }
            AsyncJobOperator operator = ctx.getBean("asyncJobOperator", AsyncJobOperator.class);
            status = operator.start(args);
        } finally {
            closeApplicationContext(ctx);
        }
        return status;
    }

    protected void closeApplicationContext(ApplicationContext context) {
        if (context == null) {
            return;
        }
        if (context instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) context;
            aac.close();
            aac.destroy();
        }
    }

    /**
     * 管理用Bean定義（基本部）ファイル名を取得する。
     * @return 管理用Bean定義（基本部）ファイル名
     */
    protected String getDefaultBeanFileName() {
        StringBuilder str = new StringBuilder();
        String classpath = PropertyUtil
                .getProperty(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY);
        String beanFileName = PropertyUtil.getProperty(BEAN_DEFINITION_DEFAULT);
        str.append(classpath == null ? "" : classpath);
        str.append(beanFileName == null ? "" : beanFileName);
        LOGGER.debug(LogId.DAL025020, str);
        return str.toString();
    }

    /**
     * 管理用Bean定義（データソース部）ファイル名を取得する。
     * @return 管理用Bean定義（データソース部）ファイル名
     */
    protected String getDataSourceBeanFileName() {
        StringBuilder str = new StringBuilder();
        String classpath = PropertyUtil
                .getProperty(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY);
        String beanFileName = PropertyUtil
                .getProperty(BEAN_DEFINITION_DATASOURCE);
        str.append(classpath == null ? "" : classpath);
        str.append(beanFileName == null ? "" : beanFileName);
        LOGGER.debug(LogId.DAL025020, str);
        return str.toString();
    }

    /**
     * <h6>アプリケーションコンテキスト取得.</h6>
     * @param batchBeanFileName Bean定義ファイル名
     * @return アプリケーションコンテキスト
     */
    protected ApplicationContext getApplicationContext(
            String... batchBeanFileName) {
        ApplicationContext ctx = null;
        Class<?> clazz = null;
        Constructor<?> constructor = null;
        try {
            // クラス読み込み
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = cl.loadClass(APPLICATION_CONTEXT);
        } catch (ClassNotFoundException e) {
            LOGGER.error(LogId.EAL025013, e);
            return null;
        }
        try {
            // コンストラクタ取得
            Class<?>[] arrClass = new Class[] { String[].class };
            constructor = clazz.getConstructor(arrClass);
        } catch (SecurityException e) {
            LOGGER.error(LogId.EAL025014, e);
            return null;
        } catch (NoSuchMethodException e) {
            LOGGER.error(LogId.EAL025015, e);
            return null;
        }
        try {
            Object[] array = new Object[] { (Object[]) batchBeanFileName };
            // コンテキストのインスタンス生成
            ctx = (ApplicationContext) constructor.newInstance(array);
            return ctx;
        } catch (IllegalArgumentException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (InstantiationException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (IllegalAccessException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (InvocationTargetException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (RuntimeException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        }
        return ctx;
    }
}
