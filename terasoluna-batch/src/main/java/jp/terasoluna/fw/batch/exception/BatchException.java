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

package jp.terasoluna.fw.batch.exception;

import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.batch.util.MessageUtil;

/**
 * バッチ例外。<br>
 * <br>
 * バッチ実行時に発生した例外情報を保持する。
 */
public class BatchException extends RuntimeException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 7677068837918514733L;

    /**
     * メッセージID
     */
    private String messageId = null;

    /**
     * 例外情報特定のためのパラメータ
     */
    private Object[] params = null;

    /**
     * BatchExceptionを生成する
     */
    public BatchException() {
        super();
    }

    /**
     * BatchExceptionを生成する
     * @param message
     */
    public BatchException(String message) {
        super(message);
    }

    /**
     * BatchExceptionを生成する
     * @param message
     * @param cause
     */
    public BatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * BatchExceptionを生成する
     * @param cause
     */
    public BatchException(Throwable cause) {
        super(cause);
    }

    /**
     * BatchExceptionを生成する
     * @param messageId エラーコード
     * @param message エラーメッセージ
     */
    public BatchException(String messageId, String message) {
        super(message);

        this.messageId = messageId;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public BatchException(String messageId, String message, Throwable cause) {
        super(message, cause);

        this.messageId = messageId;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param params 例外情報特定のためのパラメータ
     */
    public BatchException(String messageId, String message, Object... params) {
        super(message);

        this.messageId = messageId;
        this.params = params;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     * @param params 例外情報特定のためのパラメータ
     */
    public BatchException(String messageId, String message, Throwable cause,
            Object... params) {
        super(message, cause);

        this.messageId = messageId;
        this.params = params;
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     */
    public static BatchException createException(String messageId) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId));
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param params 例外情報特定のためのパラメータ
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     */
    public static BatchException createException(String messageId,
            Object... params) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId,
                params), params);
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param cause 原因となった例外
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     */
    public static BatchException createException(String messageId,
            Throwable cause) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId),
                cause);
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param cause 原因となった例外
     * @param params 例外情報特定のためのパラメータ
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     */
    public static BatchException createException(String messageId,
            Throwable cause, Object... params) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId,
                params), cause, params);
    }

    /**
     * ログ出力用文字列作成
     * @return ログ出力用文字列
     */
    public String getLogMessage() {

        StringBuilder logMsg = new StringBuilder();

        logMsg.append(BatchUtil.cat("[", messageId, "] ", getMessage()));

        if (params != null) {
            logMsg.append(" (\n");
            for (Object option : params) {
                logMsg.append(BatchUtil.cat("\t", option, "\n"));
            }
            logMsg.append(")");
        }

        return logMsg.toString();
    }

    /**
     * メッセージIDを取得.
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }
}
