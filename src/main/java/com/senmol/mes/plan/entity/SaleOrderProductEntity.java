package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 销售订单发货(SaleOrderProduct)表实体类
 *
 * @author makejava
 * @since 2023-07-18 09:15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_sale_order_product")
public class SaleOrderProductEntity extends Model<SaleOrderProductEntity> {
    private static final long serialVersionUID = 1172579436838338504L;
    /**
     * 销售订单ID
     */
    @TableField("sale_order_id")
    private Long saleOrderId;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 产品ID
     */
    @TableField(exist = false)
    private String productCode;
    /**
     * 产品ID
     */
    @TableField(exist = false)
    private String productTitle;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 客户产品编号
     */
    @TableField("cus_pro_code")
    private String cusProCode;
    /**
     * 发货数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 实际发货数量
     */
    @TableField("reality_qty")
    private BigDecimal realityQty;
    /**
     * 已发货数量
     */
    @TableField(exist = false)
    private BigDecimal sendQty;
    /**
     * 预发货数量
     */
    @TableField(exist = false)
    private BigDecimal willSendQty;
    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 税率
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

}

