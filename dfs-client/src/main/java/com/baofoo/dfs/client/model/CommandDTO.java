package com.baofoo.dfs.client.model;

import com.baofoo.dfs.client.enums.Operation;
import java.io.Serializable;

/**
 * Socket 命令请求对象
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/26
 */
public class CommandDTO implements Serializable{

    private static final long serialVersionUID = -6347736617057417599L;

    /** 操作类型：上传、下载、删除 */
    private Operation operation;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
