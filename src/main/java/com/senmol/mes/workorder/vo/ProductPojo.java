package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class ProductPojo implements Serializable {
    private static final long serialVersionUID = -4787066552473721336L;
    /**
     * 工单ID
     */
    private Long id;
    /**
     * 工单编号
     */
    private String code;
    /**
     * 工单数量
     */
    private BigDecimal qty;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 工位
     */
    private String stationTitle;
    /**
     * 良率
     */
    private BigDecimal yield;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品编号
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productTitle;
    /**
     * 建议生产日期
     */
    private LocalDateTime expectDate;
    /**
     * 需求完成时间
     */
    private LocalDateTime realityFinishTime;

}
