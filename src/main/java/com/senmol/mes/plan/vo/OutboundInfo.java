package com.senmol.mes.plan.vo;

import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class OutboundInfo implements Serializable {
    private static final long serialVersionUID = -369200266158391494L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 出库编号
     */
    private String code;
    /**
     * 关联单号父级ID
     */
    private Long pid;
    /**
     * 关联单号-工单/委外计划/销售发货单
     */
    private String orderNo;
    /**
     * 出库类型 0-生产 1-委外 2-成品 3-其他  10-发货单
     */
    private Integer type;
    /**
     * 状态 0-未打印 1-打印完成 2-已出库 3-部分发货
     */
    private Integer status;
    /**
     * 建议出库时间
     */
    private LocalDate adviceOutTime;
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
    /**
     * 产品列表
     */
    private List<SaleOrderProductEntity> products;
}
