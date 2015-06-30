package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.async.controller.AsyncJobOperator;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

    protected static final int ERROR_STATUS_CODE = 255;
    
    public static void main(String[] args) {
        AsyncBatchExecutor2 own = new AsyncBatchExecutor2();
        int status = 0;
        try {
            status = own.execute(args);
        } catch (Throwable e) {
            // TODO ログを吐く。Exceptionではなく、Errorが起きた時のステータスコードを吐く
            status = ERROR_STATUS_CODE;
        }
        System.exit(status);
    }

    protected int execute(String[] args) {
        AbstractApplicationContext ctx = null;
        int status = ERROR_STATUS_CODE;
        try {
            ctx = new ClassPathXmlApplicationContext(getBeanFileName(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY, BEAN_DEFINITION_DEFAULT),
                    getBeanFileName(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY, BEAN_DEFINITION_DATASOURCE));

            AsyncJobOperator operator = ctx.getBean("asyncJobOperator", AsyncJobOperator.class);
            status = operator.start(args);
        } catch (Exception be) {
            // TODO ログを吐く
        } finally {
            closeApplicationContext(ctx);
        }
        return status;
    }

    protected void closeApplicationContext(AbstractApplicationContext context) {
        if (context == null) {
            return;
        }
        
        AbstractApplicationContext aac = (AbstractApplicationContext) context;
        aac.close();
        aac.destroy();
    }
    
    protected String getBeanFileName(String baseName, String fileName) {
        StringBuilder str = new StringBuilder();
        String classpath = PropertyUtil
                .getProperty(baseName);
        String beanFileName = PropertyUtil.getProperty(fileName);
        str.append(classpath == null ? "" : classpath);
        str.append(beanFileName == null ? "" : beanFileName);
        LOGGER.debug(LogId.DAL025020, str);
        return str.toString();
    }

}
