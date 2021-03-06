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
    AUTHENTICATION("AUTHENTICATION","认证文件",0),

    /** 理财文件-临时文件(保存5年) **/
    FINANCE("FINANCE","理财文件",1826),

    /** 惠生钱-永久存放 **/
    HSQ_INDIVIDUAL("HSQ_INDIVIDUAL","慧省钱人脸照片",0),

    /** 银行对账文件-临时文件(保存5年) **/
    BANK_VERIFY("BANK_VERIFY","渠道对账文件",1826),

    /** 商户对账文件-临时文件(保存1年) **/
    MERCHANT_VERIFY("MERCHANT_VERIFY","商户对账文件",360),

    /** 漫道资管-永久 **/
    MANDAO_ASSET("MANDAO-ASSET","商户对账文件",0),

    /** 漫道资管-临时文件(保存30天) **/
    MANDAO_ASSET_TMP("MANDAO-ASSET-TMP","商户对账文件",30);

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
