package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MRP(Mrp)表实体类
 *
 * @author makejava
 * @since 2023-07-15 11:18:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp")
public class MrpEntity extends Model<MrpEntity> {
    private static final long serialVersionUID = 2754203501422555972L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 销售订单ID
     */
    @TableField("sale_order_id")
    private Long saleOrderId;
    /**
     * 销售订单编号
     */
    @TableField(exist = false)
    private String code;
    /**
     * 是否已计算 0-未保存 1-已保存 2-已释放
     */
    @TableField("is_count")
    private Integer isCount;
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

