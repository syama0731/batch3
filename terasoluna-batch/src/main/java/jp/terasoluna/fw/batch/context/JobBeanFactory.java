package jp.terasoluna.fw.batch.context;

import jp.terasoluna.fw.batch.blogic.BLogic;

public interface JobBeanFactory {
    
    BLogic getBLogic(String name);

}
