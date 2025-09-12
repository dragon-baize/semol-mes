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
 * 权限(Permission)表实体类
 *
 * @author makejava
 * @since 2022-11-22 13:25:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class PermissionEntity extends Model<PermissionEntity> {
    private static final long serialVersionUID = 1682463582882864659L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "权限名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 编号
     */
    @NotBlank(message = "权限编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("code")
    private String code;
    /**
     * 请求类型
     */
    @NotBlank(message = "接口类型不能为空", groups = ParamsValidate.Insert.class)
    @TableField("api_type")
    private String apiType;
    /**
     * 请求地址
     */
    @NotBlank(message = "接口地址不能为空", groups = ParamsValidate.Insert.class)
    @TableField("api_uri")
    private String apiUri;
    /**
     * 所属目录
     */
    @NotBlank(message = "所属目录不能为空", groups = ParamsValidate.Insert.class)
    @TableField("catalogue")
    private String catalogue;
    /**
     * 所属页面
     */
    @NotBlank(message = "所属页面不能为空", groups = ParamsValidate.Insert.class)
    @TableField("page")
    private String page;
    /**
     * 页面排序
     */
    @NotNull(message = "页面顺序不能为空", groups = ParamsValidate.Insert.class)
    @OrderBy(asc = true)
    @TableField("page_sort")
    private Integer pageSort;
    /**
     * 按钮排序
     */
    @NotNull(message = "按钮顺序不能为空", groups = ParamsValidate.Insert.class)
    @OrderBy(asc = true)
    @TableField("button_sort")
    private Integer buttonSort;
    /**
     * 状态 0禁用 1正常 2开放
     */
    @NotNull(message = "请选择状态", groups = ParamsValidate.Insert.class)
    @TableField("status")
    private Integer status;
    /**
     * 逻辑删除 0未删除 NULL已删除
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 创建时间
     */
    @OrderBy
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

