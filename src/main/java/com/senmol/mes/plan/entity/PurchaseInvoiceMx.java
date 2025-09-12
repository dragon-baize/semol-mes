package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * (PurchaseInvoiceMx)表实体类
 *
 * @author dragon-xiaobai
 * @since 2025-09-01 17:06:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_purchase_invoice_mx")
public class PurchaseInvoiceMx extends Model<PurchaseInvoiceMx> {
    private static final long serialVersionUID = 870033571211359211L;
    /**
     * 父ID
     */
    @NotNull(message = "缺少开票ID", groups = ParamsValidate.Update.class)
    @TableField("pid")
    private Long pid;
    /**
     * 采购单ID
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 入库ID\退货ID
     */
    @TableField("mx_id")
    private Long mxId;
    /**
     * 类型 0-收货 1-退货
     */
    @TableField("type")
    private Integer type;
    /**
     * 数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 含税价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;

}

