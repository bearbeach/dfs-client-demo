package com.baofoo.dfs.server.dal.mapper;

import com.baofoo.dfs.server.dal.model.DFSNormalFileDO;
import org.apache.ibatis.annotations.Param;


/**
 * 普通文件记录操作Mapper
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
public interface DfsNormalFileMapper extends BaseMapper<DFSNormalFileDO> {

    /**
     * 查询DFS文件记录
     *
     * @param fileName  源文件名称
     * @param orgCode   组织机构代码
     * @param fileDate  源文件日期
     * @return          普通DFS文件记录信息
     */
    DFSNormalFileDO selectByFileName(@Param(value = "fileName")String fileName,
                                     @Param(value = "orgCode")String orgCode,
                                     @Param(value = "fileDate")String fileDate);

}