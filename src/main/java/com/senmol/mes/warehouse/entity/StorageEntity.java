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
 * 入库记录(Storage)表实体类
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_storage")
public class StorageEntity extends Model<StorageEntity> {
    private static final long serialVersionUID = -1895185597749788626L;
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
     * 检测号
     */
    @TableField("si_code")
    private String siCode;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物品
     */
    @TableField(exist = false)
    private String goodsTitle;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 入库数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 标识 1-收货 2-退货
     */
    @TableField(exist = false)
    private Integer sign;
    /**
     * 单价
     */
    @TableField(exist = false)
    private BigDecimal price;
    /**
     * 含税价
     */
    @TableField(exist = false)
    private BigDecimal taxPrice;
    /**
     * 剩余量
     */
    @TableField("residue_qty")
    private BigDecimal residueQty;
    /**
     * 状态 0-过期 1-未过期 2-将过期
     */
    @TableField("status")
    private Integer status;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    @TableField("life_type")
    private Integer lifeType;
    /**
     * 寿命信息/天
     */
    @TableField("life_info")
    private Integer lifeInfo;
    /**
     * 库位ID
     */
    @TableField("stock_id")
    private Long stockId;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 数据来源 0-系统录入 1-手工录入 2-excel导入
     */
    @TableField("source")
    private Integer source;
     /**
     * 采购开票ID
     */
    @TableField("invoice")
    private Long invoice;
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

