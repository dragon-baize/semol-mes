package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dragon-xiaobai
 * @since 2025-03-21 11:47:44
 */
@Data
public class WorkOrderMxDto implements Serializable {
    private static final long serialVersionUID = 6455084566352021425L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 工单ID
     */
    private Long pid;
    /**
     * 工单批次号
     */
    private String code;
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
     * 工单数量
     */
    private BigDecimal qty;
    /**
     * 良品数量
     */
    private BigDecimal nonDefective;
    /**
     * 不良品数量
     */
    private BigDecimal defective;
    /**
     * 开工时间
     */
    private LocalDateTime beginTime;
    /**
     * 完工时间
     */
    private LocalDateTime endTime;
    /**
     * 状态 0-未打印 1-已打印
     */
    private Integer status;
    /**
     * 完成状态 0-未完成 1-完成
     */
    private Integer finish;
    /**
     * 是否释放 0-未释放 1-释放
     */
    private Integer isFree;
    /**
     * 良率
     */
    private BigDecimal yield;
    /**
     * 是否确认
     */
    private Integer isConfirm;
    /**
     * 是否领取
     */
    private Integer isReceive;
    /**
     * 是否送检 0-未送检 1-送检
     */
    private Integer submit;
    /**
     * 送检时间
     */
    private LocalDateTime submitTime;
    /**
     * 入库时间
     */
    private LocalDateTime storageTime;

}
