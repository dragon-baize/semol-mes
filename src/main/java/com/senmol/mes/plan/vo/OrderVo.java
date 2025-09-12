package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**o.id,
 o.product_id,
 o.rec_qty,
 p.order_no
 * @author Administrator
 */
@Data
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 5999892268089218932L;
    /**
     * 销售订单编号
     */
    private String code;
    /**
     * 任务单ID
     */
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 入库数量
     */
    private BigDecimal recQty;

}
