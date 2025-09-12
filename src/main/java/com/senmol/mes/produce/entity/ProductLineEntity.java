package com.senmol.mes.produce.entity;

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
 * 产线管理(ProductLine)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_product_line")
public class ProductLineEntity extends Model<ProductLineEntity> {
    private static final long serialVersionUID = 1932817538278643921L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "请输入产线名称", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 编号
     */
    @NotBlank(message = "请输入产线编号", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 状态ID
     */
    @NotNull(message = "请选择产线状态", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("status")
    private Long status;
    /**
     * 状态
     */
    @TableField(exist = false)
    private String statusTitle;
    /**
     * 产线总量
     */
    @TableField("total")
    private BigDecimal total;
    /**
     * 完成率
     */
    @TableField("rate")
    private BigDecimal rate;
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

