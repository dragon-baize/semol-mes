package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class ProduceVo implements Serializable {
    private static final long serialVersionUID = 8499241485900564174L;
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
     * 订单编号
     */
    private String orderNo;
    /**
     * 客户名称
     */
    private Long customId;
    /**
     * 客户名称
     */
    private String customTitle;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品
     */
    private String productCode;
    /**
     * 产品
     */
    private String productTitle;
    /**
     * 产品类型ID
     */
    private Integer productType;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 产线ID
     */
    private String productLineCode;
    /**
     * 产线
     */
    private String productLineTitle;
    /**
     * 产品数量
     */
    private BigDecimal productQty;
    /**
     * 已入库数量
     */
    private BigDecimal recQty;
    /**
     * 交货日期
     */
    private LocalDate deliveryDate;
    /**
     * 建议生产时间
     */
    private LocalDate expectDate;
    /**
     * 预计生产周期/H
     */
    private BigDecimal expectTakeTime;
    /**
     * 需求完成时间
     */
    private LocalDate realityFinishTime;
    /**
     * 订单图片
     */
    private String orderImg;
    /**
     * 状态 0-待定 1-生产中 2-完成
     */
    private Integer status;
    /**
     * mrp
     */
    private Integer mrp;
    /**
     * 实际生产量
     */
    private BigDecimal shiJiShengChanLiang;
    /**
     * 生产周期
     */
    private BigDecimal productCycle;
    /**
     * 每批次产品数量
     */
    private BigDecimal perBatchQty;
    /**
     * 损耗
     */
    private BigDecimal waste;
    /**
     * 是否释放 0-未释放 1-释放 2-释放中
     */
    private Integer isFree;
    /**
     * 工单总数
     */
    private Integer total;
    /**
     * 已打印工单数
     */
    private Integer printed;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
}
