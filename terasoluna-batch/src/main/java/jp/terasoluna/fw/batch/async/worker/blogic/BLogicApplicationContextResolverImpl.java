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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component("bLogicApplicationContextResolver")
public class BLogicApplicationContextResolverImpl implements BLogicApplicationContextResolver, ApplicationContextAware  {

    protected ApplicationContext parent;

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(BLogicApplicationContextResolverImpl.class);

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
    
    protected static final String REPLACE_KEY_1 = "$";
    protected static final String REPLACE_KEY_2 = "{";
    protected static final String REPLACE_KEY_3 = "}";
    
    protected static final String GETTER_PREFIX = "get";

    @Value("${beanDefinition.business.classpath}")
    protected String classpath;

    @Override
    public ApplicationContext resolveApplicationContext(BatchJobData batchJobData) {
        String bLogicBeanDefinitionName = getBeanFileName(batchJobData.getJobAppCd(), batchJobData);
        return new ClassPathXmlApplicationContext(new String[]{bLogicBeanDefinitionName}, parent);
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
    
    protected String getDirectoryReplacedRecursive(String classPath, BatchJobData jobRecord) {
        String before = null;
        String after = classPath;
        
        while(!after.equals(before)) {
            before = after;
            after = getDirectoryReplacedOnce(before, jobRecord);
        }
        
        return after;
    }
    
    protected String getDirectoryReplacedOnce(String classPath, BatchJobData jobRecord) {
        // replaceStringの代替メソッド
        // どうやら 
        // beansDef/${jobAppCode}
        // のように、beanDefinition.business.classpathで指定しておくと
        // jobAppCode=B000001
        // のとき、クラスパスが
        // beansDef/B000001
        // となる機能のようである。
        
        // 置換対象がない場合は終了
        if (classPath == null || jobRecord == null) {
            return classPath;
        }
        
        int dollerIndex = classPath.indexOf(REPLACE_KEY_1);
        int startIndex = classPath.indexOf(REPLACE_KEY_2);
        int endIndex = classPath.indexOf(REPLACE_KEY_3);
        
        // $, {, }のいずれかの文字がない場合、もしくは、${ }の順でない場合は終了
        if ( dollerIndex == -1 || 
                startIndex == -1 || 
                endIndex == -1 ||
                startIndex - dollerIndex != 1 ||
                startIndex > endIndex ||
                endIndex - startIndex < 1) {
            return classPath;
        }
        
        // 置換対象文字列を取得
        String replaceTargetPropertyNameTmp = classPath.substring(startIndex + 1, endIndex - 1);
        String replaceTargetPropertyName = null;
        boolean upperJobAppCd = REPLACE_STRING_JOB_APP_CD_UPPER.equals(replaceTargetPropertyNameTmp);
        boolean lowerJobAppCd = REPLACE_STRING_JOB_APP_CD_LOWER.equals(replaceTargetPropertyNameTmp);
        if (upperJobAppCd || lowerJobAppCd) {
            replaceTargetPropertyName = REPLACE_STRING_JOB_APP_CD;
        } else {
            replaceTargetPropertyName = replaceTargetPropertyNameTmp;
        }
        
        // 置換対象箇所の置換後文字列を取得
        PropertyDescriptor descriptor = null;
        String replaceTargetPropertyValue = null;
        try {
            descriptor = new PropertyDescriptor(replaceTargetPropertyName, jobRecord.getClass());
            Method getter = descriptor.getReadMethod();
            replaceTargetPropertyValue = (String) getter.invoke(jobRecord, (Object[]) null);
            if (upperJobAppCd) {
                replaceTargetPropertyValue = replaceTargetPropertyValue.toUpperCase();
            } else if (lowerJobAppCd) {
                replaceTargetPropertyValue = replaceTargetPropertyValue.toLowerCase();
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO 適切なログか確認
            LOGGER.error(LogId.EAL025012, e);
        }
        
        // 文字列を置換する
        return classPath.replaceAll(REPLACE_STRING_PREFIX + replaceTargetPropertyName + REPLACE_STRING_SUFFIX, replaceTargetPropertyValue);
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
