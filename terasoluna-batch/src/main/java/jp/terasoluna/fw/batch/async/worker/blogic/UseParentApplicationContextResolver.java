package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component("bLogicApplicationContextResolver")
public class UseParentApplicationContextResolver implements BLogicApplicationContextResolver, ApplicationContextAware  {

    protected boolean useParent = false;
    protected ApplicationContext parent;

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(UseParentApplicationContextResolver.class);

    /**
     * Bean定義ファイル名.
     */
    protected static final String PROPERTY_BEAN_FILENAME_SUFFIX = ".xml";

    /**
     * 置換文字列接頭語
     */
    protected static final String REPLACE_STRING_PREFIX = "\\$\\{";

    /**
     * 置換文字列：ジョブ業務コード
     */
    protected static final String REPLACE_STRING_JOB_APP_CD = "jobAppCd";

    /**
     * 置換文字列接尾語
     */
    protected static final String REPLACE_STRING_SUFFIX = "\\}";

    /**
     * 置換文字列：ジョブ業務コード（大文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_UPPER = "jobAppCdUpper";

    /**
     * 置換文字列：ジョブ業務コード（小文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_LOWER = "jobAppCdLower";

    /**
     * 置換文字列：引数の最大個数.
     */
    protected static final int REPLACE_STRING_JOB_ARG_MAX = 20;

    /**
     * 置換文字列：引数.
     */
    protected static final String REPLACE_STRING_JOB_ARG = "jobArg";

    /**
     * バッチ引数のフィールド名.
     */
    protected static final String FIELD_JOB_ARG = "JobArgNm";

    @Value("${beanDefinition.business.classpath}")
    protected String classpath;

    public void setUseParent(boolean useParent) {
        this.useParent = useParent;
    }

    @Override
    public ApplicationContext resolveApplicationContext(BatchJobData batchJobData) {
        String bLogicBeanDefinitionName = getBeanFileName(batchJobData.getJobAppCd(), batchJobData);
        if (useParent) {
            return new ClassPathXmlApplicationContext(new String[]{bLogicBeanDefinitionName}, parent);
        }
        return new ClassPathXmlApplicationContext(bLogicBeanDefinitionName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext;
    }

// TODO こっから下すっげぇ冗長・・・
    protected String getBeanFileName(String jobAppCd, BatchJobData jobRecord) {
        StringBuilder str = new StringBuilder();

        // 置換文字列を置換する
        String replacedClasspath = replaceString(classpath, jobAppCd, jobRecord);

        str.append(replacedClasspath == null ? "" : replacedClasspath);
        str.append(jobAppCd == null ? "" : jobAppCd);
        str.append(PROPERTY_BEAN_FILENAME_SUFFIX);

        return str.toString();
    }

    /**
     * 置換文字列を置換する
     * @param value 入力文字列
     * @param jobAppCd ジョブアプリケーションコード
     * @param jobRecord BatchJobData
     * @return 結果文字列
     */
    protected String replaceString(String value, String jobAppCd,
                                   BatchJobData jobRecord) {
        String result = value;

        if (result != null && jobAppCd != null && result.length() != 0
                && jobAppCd.length() != 0) {
            Map<String, String> kv = new HashMap<String, String>();
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd);

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD_UPPER);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd.toUpperCase());

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD_LOWER);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd.toLowerCase());

            for (int i = 1; i <= REPLACE_STRING_JOB_ARG_MAX; i++) {
                sb.setLength(0);
                sb.append(REPLACE_STRING_PREFIX);
                sb.append(REPLACE_STRING_JOB_ARG);
                sb.append(i);
                sb.append(REPLACE_STRING_SUFFIX);
                sb2.setLength(0);
                sb2.append("get");
                sb2.append(FIELD_JOB_ARG);
                sb2.append(i);
                kv.put(sb.toString(), (String) getMethod(jobRecord, sb2
                        .toString()));
            }

            for (Map.Entry<String, String> et : kv.entrySet()) {
                result = result.replaceAll(et.getKey(), et.getValue());
            }
        }

        return result;
    }

    /**
     * <h6>パラメータ設定.</h6>
     * @param obj 対象インスタンス
     * @param methodName メソッド名
     * @return パラメータが設定されたオブジェクト
     */
    protected Object getMethod(Object obj, String methodName) {
        Method method = null;
        Object result = null;

        if (obj == null) {
            LOGGER.error(LogId.EAL025010);
            return null;
        }

        try {
            // Beanからパラメータ取得
            method = obj.getClass().getMethod(methodName, new Class[] {});
            result = method.invoke(obj, new Object[] {});
        } catch (SecurityException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (NoSuchMethodException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (InvocationTargetException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        }
        return result;
    }

    @Override
    public void closeApplicationContext(ApplicationContext bLogicApplicationContext) {
        if (bLogicApplicationContext instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) bLogicApplicationContext;
            aac.close();
            aac.destroy();
        }
    }
}
