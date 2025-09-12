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
 * 菜单(Menu)表实体类
 *
 * @author makejava
 * @since 2023-02-25 11:13:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class MenuEntity extends Model<MenuEntity> {
    private static final long serialVersionUID = -5283608005604733461L;
    /**
     * 主键ID
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 父级ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 路径
     */
    @NotBlank(message = "菜单路径不能为空", groups = ParamsValidate.Insert.class)
    @TableField("path")
    private String path;
    /**
     * 名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("name")
    private String name;
    /**
     * 标题
     */
    @NotBlank(message = "菜单标题不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 图标
     */
    @TableField("icon")
    private String icon;
    /**
     * 类型 0-目录 1-菜单
     */
    @NotNull(message = "请选择菜单类型", groups = ParamsValidate.Insert.class)
    @TableField("type")
    private Integer type;
    /**
     * 状态 1-禁用 0-正常
     */
    @NotNull(message = "请选择菜单状态", groups = ParamsValidate.Insert.class)
    @TableField("disabled")
    private Integer disabled;
    /**
     * 不缓存 1-是 2-否
     */
    @TableField("cached")
    private Integer cached;
    /**
     * 显示顺序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 逻辑删除 0-否 NULL-是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 修改人
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

