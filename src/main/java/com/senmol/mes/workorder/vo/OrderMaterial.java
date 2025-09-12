package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-xiaobai
 * @since 2025-04-14 15:09:10
 */
@Data
public class OrderMaterial implements Serializable {
    private static final long serialVersionUID = 1397023543047946227L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String title;
    /**
     * 物品类型
     */
    private Integer type;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 工序
     */
    private Long processId;
    /**
     * 工序序号
     */
    private Integer serialNo;
    /**
     * 工序名称
     */
    private String processTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 库存量
     */
    private BigDecimal storageQty;
    /**
     * 预领取数量
     */
    private BigDecimal receiveQty;
    /**
     * 已领取数量
     */
    private BigDecimal actQty;
    /**
     * 库位ID
     */
    private Long stockId;
    /**
     * 库位
     */
    private String stockTitle;
    /**
     * 领取时间
     */
    private String createTime;
    /**
     * 批次号
     */
    private String batchNo;
}
