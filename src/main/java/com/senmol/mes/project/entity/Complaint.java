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
import java.time.LocalDateTime;

/**
 * 客诉管理(Complaint)表实体类
 *
 * @author makejava
 * @since 2023-03-22 15:05:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_complaint")
public class Complaint extends Model<Complaint> {
    private static final long serialVersionUID = -4353029719682891744L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 批次号
     */
    @NotBlank(message = "批次号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("batch_no")
    private String batchNo;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 产品编号
     */
    @NotBlank(message = "产品编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("product_code")
    private String productCode;
    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("product_title")
    private String productTitle;
    /**
     * 产品类型
     */
    @TableField("product_type")
    private String productType;
    /**
     * 产品规格
     */
    @TableField("product_specs")
    private String productSpecs;
    /**
     * 客诉时间
     */
    @NotNull(message = "请选择客诉时间", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("complain_time")
    private LocalDateTime complainTime;
    /**
     * 失效模式
     */
    @TableField("lapse_mode")
    private String lapseMode;
    /**
     * 失效个数
     */
    @TableField("lapse_num")
    private String lapseNum;
    /**
     * 客户
     */
    @TableField("customer")
    private Long customer;
    /**
     * 客户
     */
    @TableField("customer_name")
    private String customerName;
    /**
     * 关闭时间
     */
    @TableField("close_time")
    private LocalDateTime closeTime;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 8D报告文件
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 改善措施
     */
    @TableField("correction_action")
    private String correctionAction;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @OrderBy
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
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

