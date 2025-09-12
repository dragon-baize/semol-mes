package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 上料记录(WorkOrderFeed)表实体类
 *
 * @author makejava
 * @since 2023-10-23 10:44:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_feed")
public class WorkOrderFeedEntity extends Model<WorkOrderFeedEntity> {
    private static final long serialVersionUID = 1746087259034505089L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 子工单ID
     */
    @TableField("mx_id")
    private Long mxId;
    /**
     * 物料唯一码
     */
    @TableField("qr_code")
    private String qrCode;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;
    /**
     * 领取数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 使用量
     */
    @JsonIgnore
    @TableField(exist = false)
    private BigDecimal usedQty;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

