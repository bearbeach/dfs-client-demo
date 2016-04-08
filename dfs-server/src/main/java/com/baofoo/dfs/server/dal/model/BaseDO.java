package com.baofoo.dfs.server.dal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据库层公用DO
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
@Getter
@Setter
@ToString
public class BaseDO implements Serializable {

    /** 序列化UID */
    private static final long serialVersionUID = -4628855084735918294L;

    /** ID */
    private Long id;

    /** 创建人员 */
    private String createdBy = "SYSTEM";

    /** 修改人员 */
    private String updatedBy = "SYSTEM";

    /** 创建时间 */
    private Date createdAt;

    /** 修改时间 */
    private Date updatedAt;

}
