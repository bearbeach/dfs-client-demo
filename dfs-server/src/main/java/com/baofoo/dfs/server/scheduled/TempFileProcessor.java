package com.baofoo.dfs.server.scheduled;

import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.util.FastDFSUtil;
import com.baofoo.dfs.server.dal.mapper.DfsTempFileMapper;
import com.baofoo.dfs.server.dal.model.DFSTempFileDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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

    /**
     * 扫描过期文件信息（每隔半小时）
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scanFileRecord(){

        while(true){

            try{

                List<DFSTempFileDO> deadlineRecords = tempFileMapper.selectDeadlineFileRecord();
                if (deadlineRecords.isEmpty()) {
                    log.info("scan deadline record empty thread exited .");
                    break;
                }

                log.info("scan deadline record {} total .");

                delDeadlineFiles(deadlineRecords);

            }catch (Exception e){
                log.error("处理过期文件将记录信息异常：{},{}",e.getMessage(),e);
            }
        }

    }

    /**
     * 删除过期文件信息（列表）
     *
     * @param deadlineRecords       过期文件信息集合
     */
    private void delDeadlineFiles(List<DFSTempFileDO> deadlineRecords){

        if(deadlineRecords.isEmpty()){
            return;
        }

        for(DFSTempFileDO tempFileDO : deadlineRecords){

            delDeadlineFile(tempFileDO);

        }

    }

    /**
     * 删除过期文件信息
     *
     * @param tempFileDO            过期文件信息
     */
    private void delDeadlineFile(DFSTempFileDO tempFileDO){

        try{

            log.debug("delete file:{},from dfs .",tempFileDO);

            int delResult = FastDFSUtil.delete(tempFileDO.getDfsGroup(),tempFileDO.getDfsPath());
            if(delResult != 0){
                log.error("delete file:{},from dfs error !delete result = {}",tempFileDO,delResult);
                throw new DfsException(ErrorCode.DELETE_FAILURE);
            }

            log.debug("delete file:{},from dfs success !",tempFileDO);

            tempFileMapper.deleteByPrimaryKey(tempFileDO.getId());

            log.debug("delete file:{},from database success !",tempFileDO);

        }catch (Exception e){
            log.error("删除过期文件：{},异常：{},{}",tempFileDO,e.getMessage(),e);
        }

    }

}
