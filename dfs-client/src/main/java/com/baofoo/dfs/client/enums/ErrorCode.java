package com.baofoo.dfs.client.enums;

/**
 * 错误码枚举
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/26
 */
public enum ErrorCode {

    /** 请求参数为空 */
    EMPTY_PARAM("EMPTY_PARAM","请求参数为空"),

    /** 文件不存在 */
    FILE_NOT_EXITED("FILE_NOT_EXITED","文件不存在"),

    /** 文件已存在 */
    FILE_EXITED("FILE_EXITED","文件已存在"),

    /** 文件上传失败 */
    UPLOAD_FAILURE("UPLOAD_FAILURE","文件上传失败"),

    /** 文件下载失败 */
    DOWNLOAD_FAILURE("DOWNLOAD_FAILURE","文件下载失败"),

    /** 文件删除失败 */
    DELETE_FAILURE("DELETE_FAILURE","文件删除失败"),

    /** 网络连接异常 */
    NETWORK_ERROR("NETWORK_ERROR","网络连接异常"),

    /** DFS队列溢出 */
    QUEUE_FULL("QUEUE_FULL","DFS队列溢出"),

    /** 保存上传记录信息失败 */
    SAVE_UPLOAD_INFO_ERROR("SAVE_UPLOAD_INFO_ERROR","保存上传记录信息失败"),

    /** 获取上传记录信息失败 */
    GET_FILE_INFO_ERROR("GET_FILE_INFO_ERROR","获取上传记录信息失败"),

    /** 找不到上传记录 */
    FILE_INFO_NOT_FOUND("FILE_INFO_NOT_FOUND","找不到上传记录"),

    /** 参数校验错误 */
    INVALID_PARAM("INVALID_PARAM","参数校验错误"),

    /** 找不到服务提供者 */
    NO_SERVER_PROVIDER("NO_SERVER_PROVIDER","找不到服务提供者"),

    /** 系统错误 */
    SYSTEM_ERROR("SYSTEM_ERROR","系统错误");

    private String code;

    private String desc;

    ErrorCode(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
