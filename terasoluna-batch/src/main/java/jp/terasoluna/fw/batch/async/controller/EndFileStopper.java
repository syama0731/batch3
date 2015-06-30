package jp.terasoluna.fw.batch.async.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("asyncBatchStopper")
public class EndFileStopper implements AsyncBatchStopper {

    @Value("executor.endMonitoringFile")
    protected String endMonitoringFileName;

    @Override
    public boolean canStop() {
        File f = new File(endMonitoringFileName);
        return f.exists();
    }
}
