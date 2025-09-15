package com.senmol.mes.plan.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Administrator
 */
@Data
public class OrderMxPojo implements Serializable {
    private static final long serialVersionUID = -3965753683776778678L;
    /**
     * 是否已开票
     */
    private Integer mark;
    /**
     * 销售订单ID
     */
    private Long orderId;
    /**
     * 客户ID
     */
    private Long customId;
    /**
     * 经办人ID
     */
    private Long userId;
    /**
     * 产品ID
     */
    private Long productId;
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
    /**
     * 区分发货单、退货单
     */
    private Integer type;

}
