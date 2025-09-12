package com.senmol.mes.plan.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class OrderMxVo implements Serializable {
    private static final long serialVersionUID = 1090863243571877664L;
    /**
     * 销售订单ID
     */
    private Long id;
    /**
     * 销售订单编号
     */
    private String code;
    /**
     * 客户ID
     */
    private Long customId;
    /**
     * 客户
     */
    private String customTitle;
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
     * 金额
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;
    /**
     * 订货数量
     */
    private BigDecimal qty;
    /**
     * 发货数量
     */
    private BigDecimal realityQty;
    /**
     * 未发货数量
     */
    private BigDecimal unshippedQty;
    /**
     * 任务单ID
     */
    @JsonIgnore
    private Long orderId;
    /**
     * 完工入库数量
     */
    private BigDecimal recQty;
    /**
     * 下达生产数量
     */
    private BigDecimal production;
    /**
     * 不良品总数
     */
    @JsonIgnore
    private BigDecimal defective;
    /**
     * 不合格数量
     */
    @JsonIgnore
    private BigDecimal unqualifiedQty;
    /**
     * 报废数量
     */
    private BigDecimal scrapQty;

}
