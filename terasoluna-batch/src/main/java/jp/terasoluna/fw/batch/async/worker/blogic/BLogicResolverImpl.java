package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import jp.terasoluna.fw.batch.annotation.util.GenericBeanFactoryAccessorEx;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

@Component("bLogicResolver")
public class BLogicResolverImpl implements BLogicResolver {

    private static TLogger LOGGER = TLogger.getLogger(BLogicResolverImpl.class);

    /**
     * BLogicのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_BEAN_NAME_SUFFIX = "BLogic";

    /**
     * JobComponentアノテーション有効化フラグ
     */
    @Value("${enableJobComponentAnnotation}")
    protected boolean enableJobComponentAnnotation = false;

    public void setEnableJobComponentAnnotation(boolean enableJobComponentAnnotation) {
        this.enableJobComponentAnnotation = enableJobComponentAnnotation;
    }

    @Override
    public BLogic resolveBLogic(ApplicationContext ctx, String jobAppCd) {
        BLogic bLogic = null;
        if (enableJobComponentAnnotation) {
            bLogic = resolveFromAnnotation(ctx, jobAppCd);
            if (bLogic != null) {
                return bLogic;
            }
        }

        // TODO 何度もcontainsBean()してて効率悪い。
        String bLogicBeanName = getBlogicBeanName(jobAppCd);
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

    protected BLogic resolveFromAnnotation(ApplicationContext ctx, String jobAppCd) {
        GenericBeanFactoryAccessorEx gbfa = new GenericBeanFactoryAccessorEx(ctx);
        Map<String, Object> jobMap = gbfa.getBeansWithAnnotation(JobComponent.class);
        if (jobMap == null) {
            throw new NoSuchBeanDefinitionException("can't find @JobComponent on BLogic in applicationContext.");
        }
        final Set<Map.Entry<String, Object>> entries = jobMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object obj = entry.getValue();
            JobComponent jobComponent = AnnotationUtils.findAnnotation(obj.getClass(), JobComponent.class);
            if (jobComponent.jobId() == null || !jobComponent.jobId().equals(jobAppCd)) {
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
}
