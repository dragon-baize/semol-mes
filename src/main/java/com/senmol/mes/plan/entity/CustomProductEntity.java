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
 * 客户-产品(CustomProduct)表实体类
 *
 * @author makejava
 * @since 2023-07-14 15:31:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_custom_product")
public class CustomProductEntity extends Model<CustomProductEntity> {
    private static final long serialVersionUID = -4155730455942648799L;
    /**
     * 客户ID
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 产品编号
     */
    @TableField(exist = false)
    private String productCode;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productTitle;
    /**
     * 客户产品编号
     */
    @TableField("cus_pro_code")
    private String cusProCode;
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

}

