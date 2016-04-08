package com.baofoo.dfs.client.enums;

/**
 * 枚举描述
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 * @since 1.7
 */
public enum Status {

    /** 初始化 */
    INIT("INIT","初始化"),

    /** 执行中 */
    DOING("DOING ","执行中"),

    /** 成功 */
    SUCCESS("SUCCESS","成功"),

    /** 失败 */
    FAILURE("FAILURE ","失败");

    private String code;

    private String desc;

    Status(String code,String desc){
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
