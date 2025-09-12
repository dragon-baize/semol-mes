package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MRP-生产计划(MrpProduce)表实体类
 *
 * @author makejava
 * @since 2023-07-15 13:39:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp_produce")
public class MrpProduceEntity extends Model<MrpProduceEntity> {
    private static final long serialVersionUID = 6589835849406076889L;
    /**
     * MRPID
     */
    @TableField("mrp_id")
    private Long mrpId;
    /**
     * 生产计划ID
     */
    @TableField("produce_id")
    private Long produceId;

}

