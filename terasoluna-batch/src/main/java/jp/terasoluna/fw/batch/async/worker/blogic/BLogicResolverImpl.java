package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import jp.terasoluna.fw.batch.annotation.util.GenericBeanFactoryAccessorEx;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

@Component("bLogicResolver")
public class BLogicResolverImpl implements BLogicResolver, InitializingBean {

    private static TLogger LOGGER = TLogger.getLogger(BLogicResolverImpl.class);

    /**
     * BLogicのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_BEAN_NAME_SUFFIX = "BLogic";

    /**
     * JobComponentアノテーション有効化フラグ
     */
    protected boolean enableJobComponentAnnotation = false;

    /**
     * JobComponentアノテーション有効化フラグ取得キー.
     */
    protected static final String ENABLE_JOBCOMPONENT_ANNOTATION = "enableJobComponentAnnotation";

    public void setEnableJobComponentAnnotation(boolean enableJobComponentAnnotation) {
        this.enableJobComponentAnnotation = enableJobComponentAnnotation;
    }

    @Override
    public BLogic resolveBLogic(ApplicationContext ctx, BatchJobData batchJobData) {
        Assert.notNull(batchJobData);
        Assert.notNull(batchJobData.getJobAppCd());
        BLogic bLogic = null;
        if (enableJobComponentAnnotation) {
            bLogic = resolveFromAnnotation(ctx, batchJobData);
            if (bLogic != null) {
                return bLogic;
            }
        }

        // TODO 何度もcontainsBean()してて効率悪い。
        String bLogicBeanName = getBlogicBeanName(batchJobData.getJobAppCd());
        // ビジネスロジックのBeanが存在するか確認
        if (ctx.containsBean(bLogicBeanName)) {
            return ctx.getBean(bLogicBeanName, BLogic.class);
        }

        String decapitalizedName = Introspector.decapitalize(bLogicBeanName);
        try {
            bLogic = ctx.getBean(decapitalizedName, BLogic.class);
        } catch (BeansException e) {
            LOGGER.error(LogId.EAL025009, decapitalizedName);
            throw e;
        }
        return bLogic;
    }

    protected BLogic resolveFromAnnotation(ApplicationContext ctx, BatchJobData batchJobData) {
        GenericBeanFactoryAccessorEx gbfa = new GenericBeanFactoryAccessorEx(ctx);
        Map<String, Object> jobMap = gbfa.getBeansWithAnnotation(JobComponent.class);
        if (jobMap == null) {
            throw new NoSuchBeanDefinitionException("can't find @JobComponent on BLogic in applicationContext.");
        }
        final Set<Map.Entry<String, Object>> entries = jobMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object obj = entry.getValue();
            JobComponent jobComponent = AnnotationUtils.findAnnotation(obj.getClass(), JobComponent.class);
            if (jobComponent.jobId() == null || !jobComponent.jobId().equals(batchJobData.getJobAppCd())) {
                continue;
            }
            return BLogic.class.cast(entry.getValue());
        }
        return null;
    }

    /**
     * <h6>実行するBLogicのBean名を取得する.</h6>
     * @param jobAppCd ジョブアプリケーションコード
     * @return BLogicのBean名
     */
    protected String getBlogicBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // JobComponentアノテーション有効化フラグ取得
        String enableJobComponentAnnotationStr = PropertyUtil.getProperty(ENABLE_JOBCOMPONENT_ANNOTATION);
        if (enableJobComponentAnnotationStr != null && enableJobComponentAnnotationStr.length() != 0) {
            this.enableJobComponentAnnotation = Boolean.parseBoolean(enableJobComponentAnnotationStr);
        }
    }
}
