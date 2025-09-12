package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量管理(QualityManage)表实体类
 *
 * @author makejava
 * @since 2023-03-22 14:31:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_quality_manage")
public class QualityManage extends Model<QualityManage> {
    private static final long serialVersionUID = 577444119827737391L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 物料批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 物料ID
     */
    @TableField("material_id")
    private Long materialId;
    /**
     * 物料编号
     */
    @NotBlank(message = "物料编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("material_code")
    private String materialCode;
    /**
     * 物料名称
     */
    @NotBlank(message = "物料名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("material_title")
    private String materialTitle;
    /**
     * 物料类型
     */
    @TableField("material_type")
    private Long materialType;
    /**
     * 物料规格
     */
    @TableField("material_remarks")
    private String materialRemarks;
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 质量
     */
    @NotNull(message = "请选择质量", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("quality")
    private Integer quality;
    /**
     * 客诉时间
     */
    @TableField("complain_time")
    private LocalDateTime complainTime;
    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 备注
     */
    @TableField("notes")
    private String notes;
    /**
     * 原因分析
     */
    @TableField("reason")
    private String reason;
    /**
     * 改善措施
     */
    @TableField("measures")
    private String measures;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 8D报告
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 关闭时间
     */
    @TableField("close_time")
    private LocalDateTime closeTime;
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
    @OrderBy
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 创建人
     */
    @TableField(exist = false)
    private String createUserName;
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

