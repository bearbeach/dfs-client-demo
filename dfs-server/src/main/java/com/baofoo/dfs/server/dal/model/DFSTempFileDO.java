package com.baofoo.dfs.server.dal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 临时DFS文件记录信息
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DFSTempFileDO extends DFSFileDO{

    /** 序列化UID */
    private static final long serialVersionUID = 4955751206658100290L;

    /** 最后期限 */
    private Date deadline;
    
}