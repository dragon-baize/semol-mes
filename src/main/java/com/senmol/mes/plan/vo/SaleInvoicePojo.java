package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-08-22 10:18:29
 */
@Data
public class SaleInvoicePojo implements Serializable {
    private static final long serialVersionUID = -5591636265954875864L;
    /**
     * 出库单、保留品id
     */
    private Long id;
    /**
     * 类型 1-发货 2-退货 3-调价
     */
    private Integer flag;
    /**
     * 产品id
     */
    private Long goodsId;
    /**
     * 产品编号
     */
    private String goodsCode;
    /**
     * 产品名称
     */
    private String goodsTitle;
    /**
     * 产品类型
     */
    private Integer type;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 含税价单价
     */
    private BigDecimal taxPrice;

}
