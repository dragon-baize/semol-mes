package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售单开票明细(PlanSaleInvoiceMx)表实体类
 *
 * @author makejava
 * @since 2025-02-25 16:16:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_sale_invoice_mx")
public class SaleInvoiceMx extends Model<SaleInvoiceMx> {
    private static final long serialVersionUID = 2700291340907401638L;
    /**
     * 发货单开票ID
     */
    @TableField("invoice_id")
    private Long invoiceId;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 销售单ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 类型 1-发货 3-调价 2-退货
     */
    @TableField("type")
    private Integer type;
    /**
     * 出库单ID
     */
    @TableField(exist = false)
    private Long outboundId;
    /**
     * 存货ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 出库单编号
     */
    @TableField("out_code")
    private String outCode;
    /**
     * 出库时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;
    /**
     * 含税单价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;
    /**
     * 含税金额
     */
    @TableField("tax_amount")
    private BigDecimal taxAmount;
    /**
     * 价税合计
     */
    @TableField("total")
    private BigDecimal total;

}

