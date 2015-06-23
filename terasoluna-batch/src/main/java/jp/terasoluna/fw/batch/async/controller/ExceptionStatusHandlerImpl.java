package jp.terasoluna.fw.batch.async.controller;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.stereotype.Component;

@Component("exceptionStatusHandler")
public class ExceptionStatusHandlerImpl implements ExceptionStatusHandler {

    private static final TLogger LOGGER = TLogger.getLogger(ExceptionStatusHandlerImpl.class);

    @Override
    public int handleException(Exception e) {
        LOGGER.error(LogId.EAL025031, e);
        return 100;
    }
}
