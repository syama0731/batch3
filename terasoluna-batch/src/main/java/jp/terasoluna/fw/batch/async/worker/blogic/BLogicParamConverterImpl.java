package jp.terasoluna.fw.batch.async.worker.blogic;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("bLogicParamConverter")
public class BLogicParamConverterImpl implements BLogicParamConverter {

    @Resource
    protected Mapper beanMapper;

    @Override
    public BLogicParam convertBLogicParam(BatchJobData batchJobData) {
        BLogicParam bLogicParam = new BLogicParam();
        beanMapper.map(batchJobData, bLogicParam);
        return bLogicParam;
    }
}
