package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MRP-委外计划(MrpOutsource)表实体类
 *
 * @author makejava
 * @since 2023-07-16 10:29:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp_outsource")
public class MrpOutsourceEntity extends Model<MrpOutsourceEntity> {
    private static final long serialVersionUID = 5971168821346781253L;
    /**
     * MRPID
     */
    @TableField("mrp_id")
    private Long mrpId;
    /**
     * 委外计划ID
     */
    @TableField("outsource_id")
    private Long outsourceId;

}

