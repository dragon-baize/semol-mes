package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 生产工艺明细(WorkmanshipMx)表实体类
 *
 * @author dragon-xiaobai
 * @since 2025-09-08 16:48:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_workmanship_mx")
public class WorkmanshipMx extends Model<WorkmanshipMx> {
    private static final long serialVersionUID = 2006074814212098542L;
    /**
     * 父ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 版本号
     */
    @TableField("wms_version")
    private Integer wmsVersion;
    /**
     * 包装ID
     */
    @TableField("pack_id")
    private Long packId;
    /**
     * 产线ID
     */
    @TableField("product_line_id")
    private Long productLineId;
    /**
     * 说明文件
     */
    @TableField("notes")
    private String notes;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}

