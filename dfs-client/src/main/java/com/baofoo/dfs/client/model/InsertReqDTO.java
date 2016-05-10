package com.baofoo.dfs.client.model;

import com.baofoo.dfs.client.enums.FileGroup;
import com.google.common.base.Objects;
import java.util.Date;

/**
 * DFS 文件上传请求参数model
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/12/2
 */
public class InsertReqDTO extends CommandDTO {

    private static final long serialVersionUID = -5099591007208731582L;

    /** 源文件路径 */
    private String filePath;

    /** 机构代码, 银行或商家的代码~代表用户所在机构 */
    private String orgCode;

    /** 源文件名 */
    private String fileName;

    /** 源文件组 */
    private FileGroup fileGroup;

    /** 源文件创建日期 */
    private String fileDate;

    /** 备注信息 */
    private String remark;

    /** 失效日期 */
    private Date deadline;

    /** 文件大小 */
    private long fileSize;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileGroup getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(FileGroup fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("filePath", getFilePath())
                .add("operation", getOperation())
                .add("orgCode", getOrgCode())
                .add("fileName", getFileName())
                .add("fileGroup", getFileGroup())
                .add("fileDate", getFileDate())
                .add("deadline", getDeadline())
                .add("remark", getRemark())
                .add("fileSize", getFileSize())
                .omitNullValues()
                .toString();
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
