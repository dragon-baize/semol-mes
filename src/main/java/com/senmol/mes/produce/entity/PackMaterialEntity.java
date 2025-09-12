package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 包装-物料(PackMaterial)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_pack_material")
public class PackMaterialEntity extends Model<PackMaterialEntity> {
    private static final long serialVersionUID = 2078043727578504222L;
    /**
     * 包装ID
     */
    @TableField("pack_id")
    private Long packId;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;

}

