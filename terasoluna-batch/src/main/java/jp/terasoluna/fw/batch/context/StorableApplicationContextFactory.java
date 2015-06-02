package jp.terasoluna.fw.batch.context;

public interface StorableApplicationContextFactory extends
        ApplicationContextFactory {
    
    public void remove(String... beansFileName);

}
