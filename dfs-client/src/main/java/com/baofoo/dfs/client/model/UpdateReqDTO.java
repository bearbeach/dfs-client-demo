package com.baofoo.dfs.client.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * DFS 文件记录更新请求参数model
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/2
 */
public class UpdateReqDTO extends CommandDTO {

    private static final long serialVersionUID = -5099591007208731582L;

    /** 文件ID */
    private Long fileId;

    /** 在DFS的文件全路径 */
    private String dfsPath;

    /** 在DFS的文件组 */
    private String dfsGroup;

    /** DFS 下载地址 */
    private String downloadUrl;

    /** DFS 过期时间 */
    private Date deadline;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getDfsPath() {
        return dfsPath;
    }

    public void setDfsPath(String dfsPath) {
        this.dfsPath = dfsPath;
    }

    public String getDfsGroup() {
        return dfsGroup;
    }

    public void setDfsGroup(String dfsGroup) {
        this.dfsGroup = dfsGroup;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fileId", getFileId())
                .add("dfsPath", getDfsPath())
                .add("dfsGroup", getDfsGroup())
                .add("downloadUrl", getDownloadUrl())
                .add("operation", getOperation())
                .add("deadline", getDeadline())
                .omitNullValues()
                .toString();
    }

}
