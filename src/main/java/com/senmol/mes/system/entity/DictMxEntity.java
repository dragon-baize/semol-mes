package com.senmol.mes.system.entity;

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
 * 字典明细(DictMx)表实体类
 *
 * @author makejava
 * @since 2023-01-03 17:06:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_mx")
public class DictMxEntity extends Model<DictMxEntity> {
    private static final long serialVersionUID = 5155715939074973730L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 父级ID
     */
    @NotNull(message = "缺少父主键", groups = ParamsValidate.Insert.class)
    @TableField("pid")
    private Long pid;
    /**
     * 名称
     */
    @NotBlank(message = "字典明细名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
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

