package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 迁库记录(MoveRecord)表实体类
 *
 * @author makejava
 * @since 2023-12-21 11:24:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_move_record")
public class MoveRecord extends Model<MoveRecord> {
    private static final long serialVersionUID = 6626186498423137492L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 迁出批次号
     */
    @TableField("out_batch_no")
    private String outBatchNo;
    /**
     * 迁出库位ID
     */
    @TableField("out_stock_id")
    private Long outStockId;
    /**
     * 迁入批次号
     */
    @TableField("in_batch_no")
    private String inBatchNo;
    /**
     * 迁入库位ID
     */
    @TableField("in_stock_id")
    private Long inStockId;
    /**
     * 迁移数量
     */
    @TableField("qty")
    private BigDecimal qty;
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

}

