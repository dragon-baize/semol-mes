package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class OrderMxInspectVo implements Serializable {
    private static final long serialVersionUID = -1885456334184732320L;
    /**
     * 任务单ID
     */
    private Long pid;
    /**
     * 工单任务总量
     */
    private BigDecimal production;
    /**
     * 工单不良品总量
     */
    private BigDecimal defective;
    /**
     * 工单质检不合格总量
     */
    private BigDecimal unqualifiedQty;

}
