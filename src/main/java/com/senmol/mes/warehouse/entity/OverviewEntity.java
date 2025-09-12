package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 仓库总览(Overview)表实体类
 *
 * @author makejava
 * @since 2023-07-29 10:10:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_overview")
public class OverviewEntity extends Model<OverviewEntity> {
    private static final long serialVersionUID = -2567965694373015374L;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 临期数量(过期前30天)
     */
    @TableField("advent_qty")
    private BigDecimal adventQty;
    /**
     * 过期数量
     */
    @TableField("expire_qty")
    private BigDecimal expireQty;
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

