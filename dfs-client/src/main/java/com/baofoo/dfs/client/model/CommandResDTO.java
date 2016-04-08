package com.baofoo.dfs.client.model;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * DFS 文件上传响应参数model
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/2
 * @since 1.7
 */
public class CommandResDTO implements Serializable {

    private static final long serialVersionUID = -5099591007208731582L;

    /** 文件ID */
    private Long fileId;

    /** 文件名称 */
    private String fileName;

    /** 在DFS的文件全路径 */
    private String dfsPath;

    /** 在DFS的文件组 */
    private String dfsGroup;

    /** DFS 下载地址 */
    private String downloadUrl;

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fileId", getFileId())
                .add("fileName", getFileName())
                .add("dfsPath", getDfsPath())
                .add("dfsGroup", getDfsGroup())
                .add("downloadUrl", getDownloadUrl())
                .omitNullValues()
                .toString();
    }

}
