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
package jp.terasoluna.fw.message.execption;

/**
 * ���b�Z�[�W���s����O�N���X�i�����N���X�j
 *
 */
@SuppressWarnings("serial")
public class MessageRuntimeException extends RuntimeException {

    /**
     * �R���X�g���N�^
     * 
     * @param message ���b�Z�[�W
     */
    public MessageRuntimeException(String message) {
        super(message);
    }

    /**
     * �R���X�g���N�^
     * 
     * @param cause �N����O
     */
    public MessageRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * �R���X�g���N�^
     * 
     * @param message ���b�Z�[�W
     * @param cause �N����O
     */
    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}