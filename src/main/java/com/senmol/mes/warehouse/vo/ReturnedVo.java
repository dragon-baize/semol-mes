package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class ReturnedVo implements Serializable {
    private static final long serialVersionUID = 3819663436631719287L;
    /**
     * 出库单号
     */
    private String pickOrder;
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
     * 领取数量
     */
    private BigDecimal receiveQty;
    /**
     * 使用数量
     */
    private BigDecimal usedQty;
    /**
     * 生产产品量
     */
    private BigDecimal outputQty;
    /**
     * 单个产品物料量
     */
    private BigDecimal baseQty;
    /**
     * 退库数量
     */
    private BigDecimal returnQty;
    /**
     * 损耗量
     */
    private BigDecimal wastage;
    /**
     * 入库批次号
     */
    private String inBatchNo;
    /**
     * 出库批次号
     */
    private String outBatchNo;

}
