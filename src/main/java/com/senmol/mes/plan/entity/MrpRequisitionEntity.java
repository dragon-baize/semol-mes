package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MRP-请购单(MrpRequistion)表实体类
 *
 * @author makejava
 * @since 2023-07-15 11:20:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp_requisition")
public class MrpRequisitionEntity extends Model<MrpRequisitionEntity> {
    private static final long serialVersionUID = 5164361127735177445L;
    /**
     * MRPID
     */
    @TableField("mrp_id")
    private Long mrpId;
    /**
     * 请购单ID
     */
    @TableField("requisition_id")
    private Long requisitionId;

}

