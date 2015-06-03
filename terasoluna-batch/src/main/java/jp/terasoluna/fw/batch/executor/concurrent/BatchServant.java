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

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;

/**
 * バッチサーバントインタフェース。<br>
 * <br>
 * 非同期バッチエグゼキュータから呼ばれ、指定されたジョブシーケンスコードからジョブを実行する。
 */
public interface BatchServant extends Runnable {

    /**
     * ジョブシーケンスコードを設定する
     * @param jobSequenceId the jobSequenceId to set
     */
    @Deprecated
    void setJobSequenceId(String jobSequenceId);
    
    void setBLogic(BLogic blogic, BLogicParam param);
    
    void setSystemDao(SystemDao systemDao);

}
