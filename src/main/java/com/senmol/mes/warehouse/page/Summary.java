package com.senmol.mes.warehouse.page;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dragon-baize
 * @since 2025-08-07 16:53:48
 */
@Data
public class Summary implements Serializable {
    private static final long serialVersionUID = 4751879263465559422L;
    /**
     * 产品编号
     */
    private String productCode;
    /**
     * 存货ID
     */
    private Long goodsId;
    /**
     * 存货编号
     */
    private String goodsCode;
    /**
     * 存货
     */
    private String goodsTitle;
    /**
     * 存货类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 期初结存
     */
    private BigDecimal preQty = BigDecimal.ZERO;
    /**
     * 期初结存金额
     */
    private BigDecimal preAmount = BigDecimal.ZERO;
    /**
     * 本期入
     */
    private BigDecimal inQty = BigDecimal.ZERO;
    /**
     * 本期入金额
     */
    private BigDecimal inAmount = BigDecimal.ZERO;
    /**
     * 本期出
     */
    private BigDecimal outQty = BigDecimal.ZERO;
    /**
     * 本期出金额
     */
    private BigDecimal outAmount = BigDecimal.ZERO;
    /**
     * 期末结存
     */
    private BigDecimal lastQty = BigDecimal.ZERO;
    /**
     * 期末结存金额
     */
    private BigDecimal lastAmount = BigDecimal.ZERO;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime = LocalDate.now();

}
