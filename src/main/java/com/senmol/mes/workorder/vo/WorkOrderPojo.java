package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class WorkOrderPojo implements Serializable {
    private static final long serialVersionUID = 3458663298446968764L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 状态 0-未完成 1-完成
     */
    private Integer status;
    /**
     * 是否释放
     */
    private Integer isFree;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品数量
     */
    private BigDecimal productQty;
    /**
     * 已入库数量
     */
    private BigDecimal recQty;
    /**
     * 工单良品数
     */
    private BigDecimal nonDefective;
    /**
     * 工单数量
     */
    private BigDecimal qty;
    /**
     * 生产计划状态
     */
    private Integer exist;

}
