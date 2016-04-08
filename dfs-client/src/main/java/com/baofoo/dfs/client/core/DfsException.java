package com.baofoo.dfs.client.core;

import com.baofoo.dfs.client.enums.ErrorCode;

import java.io.Serializable;

/**
 * 公用异常类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/26
 * @since 1.7
 */
public class DfsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6294554723660679064L;

    private String code;

    public DfsException(ErrorCode code) {
        super(code.getDesc());
        this.code = code.getCode();
    }

    public DfsException(ErrorCode code, Throwable throwable) {
        super(throwable);
        this.code = code.getCode();
    }

    public DfsException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public DfsException(ErrorCode code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code.getCode();
    }

    public String getCode(){
        return this.code;
    }

}
