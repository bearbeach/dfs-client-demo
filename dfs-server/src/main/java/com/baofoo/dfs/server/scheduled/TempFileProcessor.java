package com.baofoo.dfs.server.scheduled;

import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.util.FastDFSUtil;
import com.baofoo.dfs.server.dal.mapper.DfsTempFileMapper;
import com.baofoo.dfs.server.dal.model.DFSTempFileDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;

/**
 * 临时文件计划任务处理
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/1
 * @since JDK 1.7
 * @see spring scheduled 3.0
 * @see spring quartz
 */
@Slf4j
@Component
public class TempFileProcessor {

    /** 临时文件记录操作Mapper */
    @Autowired
    private DfsTempFileMapper tempFileMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 扫描过期文件信息（每隔半小时）
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scanFileRecord(){

        while(true){

            try{

                int resultSize = (Integer)transactionTemplate.execute(new TransactionCallback() {
                    public Object doInTransaction(TransactionStatus status) {
                        return delDeadlineFiles();
                    }
                });

                if(resultSize < 1){
                    break;
                }

            }catch (Exception e){
                log.error("处理过期文件将记录信息异常：{},{}",e.getMessage(),e);
                break;
            }
        }

    }

    /**
     * 删除过期文件信息（列表）
     */
    private int delDeadlineFiles(){

        int resultSize = 0;

        List<DFSTempFileDO> deadlineRecords = tempFileMapper.selectDeadlineFileRecord();

        if (deadlineRecords.isEmpty()) {
            log.info("scan deadline record empty thread exited .");
            return resultSize;
        }

        log.info("scan deadline record {} total .",deadlineRecords.size());

        if(deadlineRecords.isEmpty()){
            return resultSize;
        }

        resultSize = deadlineRecords.size();

        for(DFSTempFileDO tempFileDO : deadlineRecords){

            delDeadlineFile(tempFileDO);

        }

        return resultSize;

    }

    /**
     * 删除过期文件信息
     *
     * @param tempFileDO            过期文件信息
     */
    private void delDeadlineFile(DFSTempFileDO tempFileDO){

        try{

            log.info("delete file:{},from dfs .", tempFileDO);

            if(StringUtils.isNotBlank(tempFileDO.getDfsPath())){
                int delResult = FastDFSUtil.delete(tempFileDO.getDfsGroup(),tempFileDO.getDfsPath());
                if(delResult != 0){
                    log.error("delete file:{},from dfs error !delete result = {}",tempFileDO,delResult);
                    throw new DfsException(ErrorCode.DELETE_FAILURE);
                }
            }

        }catch (DfsException e){
            log.error("删除过期文件：{},异常：{},{}",tempFileDO,e.getMessage(),e);
            throw e;
        }

        log.info("delete file:{},from dfs success !",tempFileDO);

        tempFileMapper.deleteByPrimaryKey(tempFileDO.getId());

        log.info("delete file:{},from database success !", tempFileDO);

    }

}
