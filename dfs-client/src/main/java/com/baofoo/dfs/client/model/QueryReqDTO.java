package com.baofoo.dfs.client.model;

import com.google.common.base.Objects;

/**
 * DFS 文件记录信息查询model
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/2
 * @since 1.7
 */
public class QueryReqDTO extends CommandDTO {

    private static final long serialVersionUID = -1349043277150566160L;

    /** 文件ID */
    private Long fileId;

    /** 文件名称 */
    private String fileName;

    /** 文件日期 */
    private String fileDate;

    /** 机构编码 */
    private String orgCode;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fileId", getFileId())
                .add("fileName", getFileName())
                .add("fileDate", getFileDate())
                .add("orgCode", getOrgCode())
                .add("operation", getOperation())
                .omitNullValues()
                .toString();
    }
}
