package jp.terasoluna.fw.batch.async.controller;

public interface ExceptionStatusHandler {
    int handleException(Exception e);
}
