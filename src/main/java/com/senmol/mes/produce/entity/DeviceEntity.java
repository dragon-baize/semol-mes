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
import java.time.LocalDateTime;

/**
 * 设备管理(Device)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_device")
public class DeviceEntity extends Model<DeviceEntity> {
    private static final long serialVersionUID = 6190171676262858158L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @NotBlank(message = "设备编号不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "设备名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 产线ID
     */
    @NotNull(message = "请选择产线", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("product_line_id")
    private Long productLineId;
    /**
     * 产线
     */
    @TableField(exist = false)
    private String productLineTitle;
    /**
     * 厂商
     */
    @TableField("manufacturer")
    private String manufacturer;
    /**
     * 售后人员
     */
    @TableField("after_sales_man")
    private String afterSalesMan;
    /**
     * 售后电话
     */
    @TableField("after_sales_phone")
    private String afterSalesPhone;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 状态ID
     */
    @TableField("status")
    private Long status;
    /**
     * 状态
     */
    @TableField(exist = false)
    private String statusTitle;
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

