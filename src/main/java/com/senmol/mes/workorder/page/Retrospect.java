package com.senmol.mes.workorder.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dragon-baize
 * @since 2025-08-13 10:18:06
 */
@Data
public class Retrospect implements Serializable {
    private static final long serialVersionUID = -8105376437234218611L;
    /**
     * 工单ID
     */
    @JsonIgnore
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 生产数量
     */
    @JsonIgnore
    private BigDecimal qty;
    /**
     * 出库唯一码
     */
    @JsonIgnore
    private String qrCode;
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
     * 物料类型
     */
    private Integer type;
    /**
     * 物料使用量
     */
    private BigDecimal usedQty;
    /**
     * 物料含税价
     */
    @JsonIgnore
    private BigDecimal taxPrice;
    /**
     * 合计价格
     */
    private BigDecimal totalPrice;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

}
