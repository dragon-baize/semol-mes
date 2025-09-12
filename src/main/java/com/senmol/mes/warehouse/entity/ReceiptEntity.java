package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收货管理(Receipt)表实体类
 *
 * @author makejava
 * @since 2023-02-13 15:41:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_receipt")
public class ReceiptEntity extends Model<ReceiptEntity> {
    private static final long serialVersionUID = 762301372657731032L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 计划编号
     */
    @TableField("plan_order_no")
    private String planOrderNo;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品ID
     */
    @TableField(exist = false)
    private String goodsCode;
     /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 计划收货数量
     */
    @TableField("plan_qty")
    private BigDecimal planQty;
    /**
     * 入库数量
     */
    @TableField("storage_qty")
    private BigDecimal storageQty;
    /**
     * 本次收货数量
     */
    @TableField("current_qty")
    private BigDecimal currentQty;
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
     * 类别 0-委外计划 1-采购单 2-客供货 3-其他
     */
    @TableField("sign")
    private Integer sign;
    /**
     * 状态 0-等待收货 1-已收货 2-送检
     */
    @TableField("status")
    private Integer status;
     /**
     * 是否已完成 0-否 1-是
     */
    @TableField("is_finish")
    private Integer isFinish;
    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

