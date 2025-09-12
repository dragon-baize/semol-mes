package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class OrderMaterialInfo implements Serializable {
    private static final long serialVersionUID = -2153518099554886197L;
    /**
     * 物料ID
     */
    private Long materialId;
    /**
     * 物料编号
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialTitle;
    /**
     * 物品类型
     */
    private Integer type;
    /**
     * 入库批次号
     */
    private String iBatchNo;
    /**
     * 入库时间
     */
    private LocalDateTime iCreateTime;
    /**
     * 入库人ID
     */
    private Long iCreateUser;
    /**
     * 入库人
     */
    private String iCreateUserName;
    /**
     * 出库批次号
     */
    private String oBatchNo;
    /**
     * 出库时间
     */
    private LocalDateTime oCreateTime;
    /**
     * 出库人ID
     */
    private Long oCreateUser;
    /**
     * 出库人
     */
    private String oCreateUserName;
}
