package com.baofoo.dfs.server.dal.mapper;

import com.baofoo.dfs.server.dal.model.DFSTempFileDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 临时文件记录操作Mapper
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
public interface DfsTempFileMapper extends BaseMapper<DFSTempFileDO> {

    /**
     * 查询DFS文件记录
     *
     * @param fileName  源文件名称
     * @param orgCode   组织机构代码
     * @param fileDate  源文件日期
     * @return          临时DFS文件记录信息
     */
    DFSTempFileDO selectByFileName(@Param(value = "fileName")String fileName,
                                   @Param(value = "orgCode")String orgCode,
                                   @Param(value = "fileDate")String fileDate);

    /**
     * 查询过期文件
     *
     * @return          返回过期文件记录信息列表
     */
    List<DFSTempFileDO> selectDeadlineFileRecord();

}