package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 出库单明细(OutboundMx)表实体类
 *
 * @author makejava
 * @since 2023-07-28 18:39:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_outbound_mx")
public class OutboundMxEntity extends Model<OutboundMxEntity> {
    private static final long serialVersionUID = -9115486568156683174L;
    /**
     * 出库单ID
     */
    @TableField("outbound_id")
    private Long outboundId;
    /**
     * 入库批次号
     */
    @NotBlank(message = "缺少入库批次号")
    @TableField("storage_id")
    private Long storageId;
    /**
     * 入库批次号
     */
    @TableField(exist = false)
    private String batchNo;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品编号
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物品
     */
    @TableField(exist = false)
    private String goodsTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 出库数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 实际出库数量
     */
    @TableField("act_qty")
    private BigDecimal actQty;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 库存量
     */
    @TableField(exist = false)
    private BigDecimal storageQty;
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

