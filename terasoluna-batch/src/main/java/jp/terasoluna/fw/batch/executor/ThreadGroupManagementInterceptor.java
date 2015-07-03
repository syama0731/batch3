package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.async.controller.BatchServantTaskInvoker;
import jp.terasoluna.fw.batch.async.controller.TaskExecutorDelegate;
import jp.terasoluna.fw.batch.async.worker.BLogicExecutor;
import jp.terasoluna.fw.batch.executor.AsyncBatchExecutor;
import jp.terasoluna.fw.batch.executor.ThreadGroupApplicationContextHolder;
import jp.terasoluna.fw.batch.executor.concurrent.BatchServant;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
 import java.util.concurrent.ConcurrentHashMap;

 /**
 * スレッドローカルと名称の解決、スレッドローカルのライフサイクルを一元管理するインターセプタ。
 * Bean定義内で異なるスレッドで実行される<code>TaskExecutorDelegate#execute(),BLogicExecutor#execute()</code>の
 * どちらにも適用されるよう設定すること。<br>
 * 設定例：
 * <code><pre>
 * &lt;bean id="threadGroupManagementInterceptor" class="jp.terasoluna.fw.batch.async.aop.ThreadGroupManagementInterceptor"/&gt;
 * &lt;aop:config&gt;
 *     &lt;aop:pointcut id="threadGroupManagementPointcut"
 *         expression="execution(*  jp.terasoluna.fw.batch.async.controller.TaskExecutorDelegate.execute(..)) ||
 *         execution(* jp.terasoluna.fw.batch.async.worker.BatchServant2.execute(..))" /&gt;
 *     &lt;aop:advisor advice-ref="threadGroupManagementInterceptor" pointcut-ref="threadGroupManagementPointcut"/&gt;
 * &lt;/aop:config&gt;
 * </pre></code>
 */
public class ThreadGroupManagementInterceptor implements MethodInterceptor, InitializingBean {

    /** スレッドグループプリフィックス. */
    public static final String THREAD_GROUP_PREFIX = AsyncBatchExecutor.class.getSimpleName() + "ThreadGroup";

    /** スレッド名プリフィックス. */
    public static final String THREAD_NAME_PREFIX = AsyncBatchExecutor.class.getSimpleName() + "Thread";

    /** スレッドグループセパレータ. */
    public static final String THREAD_GROUP_SEPARATOR = "-";

    /** スレッド名セパレータ. */
    public static final String THREAD_NAME_SEPARATOR = "-";

    @Value("${batchTaskExecutor.maxPoolSize:-1}")
    protected int maxPoolSize;

    @Resource
    protected MessageAccessor messageAccessor;

    protected Map<ThreadGroup, String> threadGroupMap = null;

    protected ArrayBlockingQueue<ThreadGroup> threadGroupQueue;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object target = invocation.getThis();
        Method method = invocation.getMethod();
        if (target instanceof TaskExecutorDelegate && "execute".equals(method.getName())) {
            return invokeTaskExecutor(invocation);
        }
        if (target instanceof BLogicExecutor && "execute".equals(method.getName())) {
            return invokeBLogicExecutor(invocation);
        }
        if (target instanceof BatchServant && "run".equals(method.getName())) {
            return invokeBatchServant(invocation);
        }
        throw new IllegalStateException(String.format("invocation target is invalid. [%s]", target));
    }

    protected Object invokeTaskExecutor(MethodInvocation invocation) throws Throwable {
        TaskExecutorDelegate taskExecutorDelegate = TaskExecutorDelegate.class.cast(invocation.getThis());
        ThreadGroup threadGroup = threadGroupQueue.take();
        String threadNamePrefix = threadGroupMap.get(threadGroup);
        taskExecutorDelegate.getThreadPoolTaskExecutor().setThreadGroup(threadGroup);
        taskExecutorDelegate.getThreadPoolTaskExecutor().setThreadNamePrefix(threadNamePrefix);
        return invocation.proceed();
    }

    protected Object invokeBLogicExecutor(MethodInvocation invocation) throws Throwable {
        try {
            MessageUtil.setMessageAccessor(messageAccessor);
            // スレッドグループローカルに子ApplicationContextを格納する。
            if (invocation.getArguments().length > 0 && invocation.getArguments()[0] instanceof ApplicationContext) {
                ThreadGroupApplicationContextHolder.setApplicationContext(
                        ApplicationContext.class.cast(invocation.getArguments()[0]));
            }
            return invocation.proceed();
        } finally {
            MessageUtil.removeMessageAccessor();
            ThreadGroupApplicationContextHolder.removeApplicationContext();
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            threadGroupQueue.put(threadGroup);
        }
    }

    protected Object invokeBatchServant(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            // MessageUtil,ThreadGroupApplicationContextHolderの開放処理はBatchServantに任せる。
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            threadGroupQueue.put(threadGroup);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(maxPoolSize > 0, "maxPoolSize must be defined in property file. [" + maxPoolSize + "]");

        threadGroupMap = new ConcurrentHashMap<>();
        threadGroupQueue = new ArrayBlockingQueue<>(maxPoolSize);

        for (int i = 1; i <= maxPoolSize; i++) {
            // スレッドグループプリフィックス設定
            StringBuilder tgn = new StringBuilder();
            tgn.append(THREAD_GROUP_PREFIX);
            tgn.append(THREAD_GROUP_SEPARATOR);
            tgn.append(i);

            // スレッド名プリフィックス設定
            StringBuilder tn = new StringBuilder();
            tn.append(THREAD_NAME_PREFIX);
            tn.append(THREAD_NAME_SEPARATOR);
            tn.append(i);
            tn.append(THREAD_NAME_SEPARATOR);

            ThreadGroup threadGroup = new ThreadGroup(tgn.toString());
            threadGroupMap.put(threadGroup, tn.toString());
            threadGroupQueue.add(threadGroup);
        }
    }
}
