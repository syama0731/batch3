/*
 * Copyright (c) 2011 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.executor.concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * バッチジョブ実行用スレッドプールエグゼキュータ。<br>
 */
public class BatchThreadPoolTaskExecutor extends ThreadPoolTaskExecutor implements Runnable {

    /**
     * シリアル番号
     */
    private static final long serialVersionUID = -2981596095578598598L;

    private static final Logger logger = LoggerFactory.getLogger(BatchThreadPoolTaskExecutor.class);
    
    protected Semaphore semaphore;
    
    protected Set<Future<?>> futureSet = Collections.synchronizedSet(new HashSet<Future<?>>());

    /**
     * コンストラクタ
     */
    public BatchThreadPoolTaskExecutor() {
        super();
        
        ExecutorService es = Executors.newSingleThreadExecutor(this);
        
        es.submit(this);
    }
    
    /*
     * (non-Javadoc)
     * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor#setMaxPoolSize(int)
     */
    @Override
    public void setMaxPoolSize(int maxPoolSize) {
        super.setMaxPoolSize(maxPoolSize);
        super.setQueueCapacity(maxPoolSize);
        this.semaphore = new Semaphore(maxPoolSize, true);
    }
    
    /*
     * (non-Javadoc)
     * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor#execute(java.lang.Runnable)
     */
    @Override
    public void execute(Runnable task) {
        ExecutorService executor = getThreadPoolExecutor();
        try {
            semaphore.acquire();
            futureSet.add(executor.submit(task));
            if(logger.isDebugEnabled()) {
                logger.debug("add thread, semaphore available permits is {}", semaphore.availablePermits());
            }
        }
        catch (RejectedExecutionException | InterruptedException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    /**
     * 
     */
    @Override
    public void run() {
        
        Iterator<Future<?>> iterator;
        Future<?> future;
        Set<Future<?>> removeFutureSet = Collections.synchronizedSet(new HashSet<Future<?>>());
        
        while(true) {
            synchronized(this.futureSet) {
                iterator = futureSet.iterator();
                
                while(iterator.hasNext()) {
                    future = iterator.next();
                    if(future.isDone()) {
                        semaphore.release();
                        removeFutureSet.add(future);
                        if(logger.isDebugEnabled()) {
                            logger.debug("remove thread, semaphore available permits is {}", semaphore.availablePermits());
                        }
                    }
                }
            }
            futureSet.removeAll(removeFutureSet);
            removeFutureSet.clear();
        }
    }

}
