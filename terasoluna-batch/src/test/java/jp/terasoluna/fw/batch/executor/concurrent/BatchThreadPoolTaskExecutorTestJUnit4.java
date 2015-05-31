package jp.terasoluna.fw.batch.executor.concurrent;

import static org.junit.Assert.*;

import javax.inject.Inject;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:beansDef/AdminContext.xml")
public class BatchThreadPoolTaskExecutorTestJUnit4 {
    
    private static final Logger logger = LoggerFactory.getLogger(BatchThreadPoolTaskExecutorTestJUnit4.class);
    
    @Inject
    private BatchThreadPoolTaskExecutor ex;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExecuteRunnable() {
        ex.setCorePoolSize(2);
        ex.setMaxPoolSize(2);
        
        Runnable run1 = new Runnable() {
            
            @Override
            public void run() {
                BLogicParam param = new BLogicParam();
                param.setJobAppCd("test1");
                
                BLogic job = new BLogic(){
                    @Override
                    public int execute(BLogicParam param) {
                        try {
                            logger.info(param.getJobAppCd());
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        return 0;
                    }
                };
                
                job.execute(param);
                logger.info("job1 end");
            }
        };
        Runnable run2 = new Runnable() {
            
            @Override
            public void run() {
                BLogicParam param = new BLogicParam();
                param.setJobAppCd("test2");
                
                BLogic job = new BLogic(){
                    @Override
                    public int execute(BLogicParam param) {
                        try {
                            logger.info(param.getJobAppCd());
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        return 0;
                    }
                };
                
                job.execute(param);
                logger.info("job2 end");
            }
        };
        Runnable run3 = new Runnable() {
            
            @Override
            public void run() {
                BLogicParam param = new BLogicParam();
                param.setJobAppCd("test3");
                
                BLogic job = new BLogic(){
                    @Override
                    public int execute(BLogicParam param) {
                        try {
                            logger.info(param.getJobAppCd());
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        return 0;
                    }
                };
                
                job.execute(param);
                logger.info("job3 end");
            }
        };
        Runnable run4 = new Runnable() {
            
            @Override
            public void run() {
                BLogicParam param = new BLogicParam();
                param.setJobAppCd("test4");
                
                BLogic job = new BLogic(){
                    @Override
                    public int execute(BLogicParam param) {
                        try {
                            logger.info(param.getJobAppCd());
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        return 0;
                    }
                };
                
                job.execute(param);
                logger.info("job4 end");
            }
        }; 
        
        ex.execute(run1);
        ex.execute(run2);
        ex.execute(run3);
        ex.execute(run4);

    }

}
