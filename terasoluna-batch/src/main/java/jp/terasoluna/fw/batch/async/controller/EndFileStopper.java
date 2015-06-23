package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("asyncBatchStopper")
public class EndFileStopper implements AsyncBatchStopper {

    private static final String EXECUTOR_END_MONITORING_FILE = PropertyUtil.getProperty("executor.endMonitoringFile");

    @Override
    public boolean canStop() {
        File f = new File(EXECUTOR_END_MONITORING_FILE);
        return f.exists();
    }
}
