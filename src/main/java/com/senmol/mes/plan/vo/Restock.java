package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dragon-baize
 * @since 2025-05-16 15:59:38
 */
@Data
public class Restock implements Serializable {
    private static final long serialVersionUID = 4640139791424515439L;
    /**
     * 收退货 1-收 2-退
     */
    private Integer receipt;
    /**
     * 是否已开票 0-否 1-是
     */
    private Integer mark;
    /**
     * 收退货ID
     */
    private Long id;
    /**
     * 收退货批次号
     */
    private String batchNo;
    /**
     * 采购单ID
     */
    private Long pid;
    /**
     * 采购单号
     */
    private String orderNo;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    private String goodsCode;
    /**
     * 物品名称
     */
    private String goodsTitle;
    /**
     * 物品类型
     */
    private Integer type;
    /**
     * 收退货数量
     */
    private BigDecimal qty;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;
    /**
     * 经办时间
     */
    private LocalDateTime createTime;
    /**
     * 经办人ID
     */
    private Long createUser;
    /**
     * 经办人
     */
    private String createUserName;

}
