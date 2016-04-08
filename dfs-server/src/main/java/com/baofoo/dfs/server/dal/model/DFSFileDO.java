package com.baofoo.dfs.server.dal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公用DFS文件记录信息
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DFSFileDO extends BaseDO {

    /** 序列化UID */
    private static final long serialVersionUID = -489256149327469265L;

    /** 机构代码, 银行或商家的代码~代表用户所在机构 */
    private String orgCode;

    /** 源文件名 */
    private String fileName;

    /** 源文件组 */
    private String fileGroup;

    /** 源文件创建日期 */
    private String fileDate;

    /** 在DFS的文件全路径 */
    private String dfsPath;

    /** 在DFS的文件组 */
    private String dfsGroup;

    /** 备注信息 */
    private String remark;

}
