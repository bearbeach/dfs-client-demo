package com.baofoo.dfs.client.enums;

/**
 * 操作类型枚举
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/26
 * @since 1.7
 */
public enum Operation {

    INSERT("INSERT","增加文件记录"),

    QUERY("QUERY","查询记录"),

    UPDATE("UPDATE","更新记录"),

    DELETE("DELETE","删除记录");

    private String code;

    private String desc;

    Operation(String code,String desc){
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
