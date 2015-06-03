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

import javax.inject.Inject;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.constant.BatchJobStatusConstant;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;


public class BatchServantImpl implements BatchServant {
    
    private String jobSequenceId;
    
    private BLogic blogic;
    
    private BLogicParam param;
    
    private SystemDao systemDao;

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // バッチ実行
        Integer result = this.blogic.execute(param);
        
        BatchJobManagementUpdateParam batchJobManagementUpdateParam = new BatchJobManagementUpdateParam();
        batchJobManagementUpdateParam.setBLogicAppStatus(result.toString());
        batchJobManagementUpdateParam.setJobSequenceId(param.getJobSequenceId());
        batchJobManagementUpdateParam.setCurAppStatus(BatchJobStatusConstant.FINISHED_STATUS);
        systemDao.updateJobTable(batchJobManagementUpdateParam);
    }

    @Override
    public void setJobSequenceId(String jobSequenceId) {
        this.jobSequenceId = jobSequenceId;
        
        // ジョブ管理テーブルからBLogicとBLogicParamを取得し、セットする
    }

    @Override
    public void setBLogic(BLogic blogic, BLogicParam param) {
        this.blogic = blogic;
        
    }

    @Override
    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }
}
