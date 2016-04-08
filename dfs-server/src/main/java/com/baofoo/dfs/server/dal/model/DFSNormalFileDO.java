package com.baofoo.dfs.server.dal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 普通DFS文件记录信息
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DFSNormalFileDO extends DFSFileDO {

    /** 序列化UID */
    private static final long serialVersionUID = -5708875464150410916L;

}
