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
public class RequisitionVo implements Serializable {
    private static final long serialVersionUID = -7128862648360683734L;
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
     * 产品ID
     */
    private Long productId;
    /**
     * 销售订单ID
     */
    private Long saleOrderId;
    /**
     * 销售订单编号
     */
    private String saleOrderCode;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 需求时间
     */
    private LocalDate demandTime;
    /**
     * 采购周期
     */
    private BigDecimal purchaseCycle;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 建议采购日期
     */
    private LocalDate advicePurchaseDate;
    /**
     * 建议采购数量
     */
    private BigDecimal adviceQty;
    /**
     * 状态 0-未生成 1-已生成
     */
    private Integer status;
    /**
     * 是否MRP导入 0-是 1-否
     */
    private Integer mrp;
    /**
     * mrp_id
     */
    private Long mrpId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建人
     */
    private String createUserName;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
    /**
     * 版本号
     */
    private Integer version;

}
