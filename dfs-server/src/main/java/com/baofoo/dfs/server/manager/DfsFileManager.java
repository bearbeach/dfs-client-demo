package com.baofoo.dfs.server.manager;

import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.model.*;
import com.baofoo.dfs.server.dal.mapper.DfsNormalFileMapper;
import com.baofoo.dfs.server.dal.mapper.DfsTempFileMapper;
import com.baofoo.dfs.server.dal.model.DFSNormalFileDO;
import com.baofoo.dfs.server.dal.model.DFSTempFileDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * DFS 文件记录管理接口
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/1
 * @since 1.7
 */
@Slf4j
@Component
public class DfsFileManager {

    /** 普通文件记录操作Mapper */
    @Autowired
    private DfsNormalFileMapper normalFileMapper;

    /** 临时文件记录操作Mapper */
    @Autowired
    private DfsTempFileMapper   tempFileMapper;

    /**
     * 新增DFS 文件记录信息
     *
     * @param insertReqDTO   Socket 命令请求对象
     * @return               文件记录ID
     */
    public Long insert(InsertReqDTO insertReqDTO){
        try{
            if(insertReqDTO.getDeadline() == null){
                DFSNormalFileDO normalFileDO = new DFSNormalFileDO();
                normalFileDO.setFileSize(insertReqDTO.getFileSize());
                normalFileDO.setOrgCode(insertReqDTO.getOrgCode());
                normalFileDO.setFileName(insertReqDTO.getFileName());
                normalFileDO.setRemark(insertReqDTO.getRemark());
                normalFileDO.setFileDate(insertReqDTO.getFileDate());
                normalFileDO.setFileGroup(insertReqDTO.getFileGroup().getCode());
                int insertRows = normalFileMapper.insert(normalFileDO);
                if(insertRows != 1){
                    throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
                }

                return normalFileDO.getId();
            }


            DFSTempFileDO tempFileDO = new DFSTempFileDO();
            tempFileDO.setFileSize(insertReqDTO.getFileSize());
            tempFileDO.setOrgCode(insertReqDTO.getOrgCode());
            tempFileDO.setFileName(insertReqDTO.getFileName());
            tempFileDO.setRemark(insertReqDTO.getRemark());
            tempFileDO.setFileDate(insertReqDTO.getFileDate());
            tempFileDO.setDeadline(insertReqDTO.getDeadline());
            tempFileDO.setFileGroup(insertReqDTO.getFileGroup().getCode());

            int insertRows = tempFileMapper.insert(tempFileDO);
            if(insertRows != 1){
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
            }

            return tempFileDO.getId();

        }catch (DuplicateKeyException e){
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.FILE_EXITED);
        }
    }

    /**
     * 新增DFS 文件记录信息
     * 新功能
     *
     * @param insertReqSTO   Socket 命令请求对象
     * @return               文件记录ID
     */
    public Long insert(InsertReqSTO insertReqSTO){
        try{
            if(insertReqSTO.getDeadline() == null){
                DFSNormalFileDO normalFileDO = new DFSNormalFileDO();
                normalFileDO.setFileSize(insertReqSTO.getFileSize());
                normalFileDO.setOrgCode(insertReqSTO.getOrgCode());
                normalFileDO.setFileName(insertReqSTO.getFileName());
                normalFileDO.setRemark(insertReqSTO.getRemark());
                normalFileDO.setFileDate(insertReqSTO.getFileDate());
                normalFileDO.setFileGroup(insertReqSTO.getFileGroup());
                int insertRows = normalFileMapper.insert(normalFileDO);
                if(insertRows != 1){
                    throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
                }

                return normalFileDO.getId();
            }


            DFSTempFileDO tempFileDO = new DFSTempFileDO();
            tempFileDO.setFileSize(insertReqSTO.getFileSize());
            tempFileDO.setOrgCode(insertReqSTO.getOrgCode());
            tempFileDO.setFileName(insertReqSTO.getFileName());
            tempFileDO.setRemark(insertReqSTO.getRemark());
            tempFileDO.setFileDate(insertReqSTO.getFileDate());
            tempFileDO.setDeadline(insertReqSTO.getDeadline());
            tempFileDO.setFileGroup(insertReqSTO.getFileGroup());

            int insertRows = tempFileMapper.insert(tempFileDO);
            if(insertRows != 1){
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
            }

            return tempFileDO.getId();

        }catch (DuplicateKeyException e){
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.FILE_EXITED);
        }
    }

    /**
     * 更新DFS 文件记录信息
     *
     * @param command   Socket 命令请求对象
     */
    public void update(UpdateReqDTO command){
        if(command.getDeadline() == null){
            DFSNormalFileDO normalFileDO = new DFSNormalFileDO();
            normalFileDO.setId(command.getFileId());
            normalFileDO.setDfsGroup(command.getDfsGroup());
            normalFileDO.setDfsPath(command.getDfsPath());
            int updateRows = normalFileMapper.update(normalFileDO);
            if(updateRows != 1){
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
            }
            return;
        }


        DFSTempFileDO tempFileDO = new DFSTempFileDO();
        tempFileDO.setId(command.getFileId());
        tempFileDO.setDfsGroup(command.getDfsGroup());
        tempFileDO.setDfsPath(command.getDfsPath());

        int updateRows = tempFileMapper.update(tempFileDO);

        if(updateRows != 1){
            throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR);
        }
    }

    /**
     * 查询文件信息
     *
     * @param command   Socket 命令请求对象
     * @return          DFS文件记录信息
     */
    public CommandResDTO queryFileInfo(QueryReqDTO command){
        if(null != command.getFileId()){
            return queryByFileId(command.getFileId());
        }
        return queryByFile(command.getFileName(),command.getOrgCode(),command.getFileDate());
    }

    /**
     * 查询文件信息
     *
     * @param fileId            文件记录ID
     * @return                  DFS文件记录信息
     */
    public CommandResDTO queryByFileId(Long fileId){
        CommandResDTO commandResDTO = new CommandResDTO();
        DFSNormalFileDO normalFileDO = normalFileMapper.selectByPrimaryKey(fileId);
        if(null != normalFileDO){
            commandResDTO.setDfsPath(normalFileDO.getDfsPath());
            commandResDTO.setFileName(normalFileDO.getFileName());
            commandResDTO.setDfsGroup(normalFileDO.getDfsGroup());
            commandResDTO.setFileId(normalFileDO.getId());
            commandResDTO.setFileSize(normalFileDO.getFileSize());
            return commandResDTO;
        }

        DFSTempFileDO tempFileDO = tempFileMapper.selectByPrimaryKey(fileId);
        if(null != tempFileDO){
            commandResDTO.setDfsPath(tempFileDO.getDfsPath());
            commandResDTO.setFileName(tempFileDO.getFileName());
            commandResDTO.setDfsGroup(tempFileDO.getDfsGroup());
            commandResDTO.setFileId(tempFileDO.getId());
            commandResDTO.setFileSize(tempFileDO.getFileSize());
            return commandResDTO;
        }

        throw new DfsException(ErrorCode.FILE_INFO_NOT_FOUND);
    }

    /**
     * 查询文件信息
     *
     * @param fileName          源文件名
     * @param orgCode           组织机构代码
     * @param fileDate          源文件日起
     * @return                  DFS文件记录信息
     */
    public CommandResDTO queryByFile(String fileName,String orgCode,String fileDate){
        CommandResDTO commandResDTO = new CommandResDTO();
        DFSNormalFileDO normalFileDO = normalFileMapper.selectByFileName(fileName,orgCode,fileDate);
        if(null != normalFileDO){
            commandResDTO.setDfsPath(normalFileDO.getDfsPath());
            commandResDTO.setFileName(normalFileDO.getFileName());
            commandResDTO.setDfsGroup(normalFileDO.getDfsGroup());
            commandResDTO.setFileId(normalFileDO.getId());
            return commandResDTO;
        }

        DFSTempFileDO tempFileDO = tempFileMapper.selectByFileName(fileName,orgCode,fileDate);
        if(null != tempFileDO){
            commandResDTO.setDfsPath(tempFileDO.getDfsPath());
            commandResDTO.setFileName(tempFileDO.getFileName());
            commandResDTO.setDfsGroup(tempFileDO.getDfsGroup());
            commandResDTO.setFileId(tempFileDO.getId());
            return commandResDTO;
        }

        throw new DfsException(ErrorCode.FILE_INFO_NOT_FOUND);
    }

    /**
     * 删除文件记录信息
     *
     * @param deleteReqDTO    Socket 命令请求对象
     */
    public void delete(DeleteReqDTO deleteReqDTO){
        int delRows = normalFileMapper.deleteByPrimaryKey(deleteReqDTO.getFileId());
        if(delRows != 1){
            delRows = tempFileMapper.deleteByPrimaryKey(deleteReqDTO.getFileId());
            if(delRows != 1){
                throw new DfsException(ErrorCode.FILE_INFO_NOT_FOUND);
            }
        }
    }

}
