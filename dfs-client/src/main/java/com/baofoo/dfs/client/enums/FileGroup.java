package com.baofoo.dfs.client.enums;

/**
 * DFS 文件组枚举类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 */
public enum FileGroup {

    /** 清算对账文件-临时文件（保存90天） */
    CLEARING("CLEARING","清算对账文件",90),

    /** 交易明细文件-临时文件（保存7天） */
    TRADE_INFO("TRADE_INFO","交易明细文件",7),

    /** 产品文件-永久存放 */
    PRODUCT("PRODUCT","产品文件",0),

    /** 认证文件-永久存放 */
    AUTHENTICATION("AUTHENTICATION","认证文件",0);

    private String code;

    private String desc;

    private int day;

    FileGroup(String code,String desc,int day){
        this.code = code;
        this.desc = desc;
        this.day = day;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

}
