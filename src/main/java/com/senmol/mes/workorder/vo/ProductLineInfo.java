package com.senmol.mes.workorder.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class ProductLineInfo implements Serializable {
    private static final long serialVersionUID = 8028021693879105384L;
    /**
     * 工单ID列表
     */
    @JsonIgnore
    private String orderIds;
    /**
     * 产线ID
     */
    private Long id;
    /**
     * 产线编号
     */
    private String code;
    /**
     * 产线名称
     */
    private String title;
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
     * 预计数量(生产计划数量)
     */
    private BigDecimal planQty;
    /**
     * 生产数量(工单数量)
     */
    private BigDecimal orderQty;
    /**
     * 总良品数
     */
    private BigDecimal nonDefective;
    /**
     * 总不良品数
     */
    private BigDecimal defective;

}
