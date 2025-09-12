package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工位管理(Station)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_station")
public class StationEntity extends Model<StationEntity> {
    private static final long serialVersionUID = -7940127344277357858L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "工位名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 密码
     */
    @JsonIgnore
    @TableField(value = "password", updateStrategy = FieldStrategy.NEVER)
    private String password;
    /**
     * 产线ID
     */
    @NotNull(message = "请选择产线", groups = ParamsValidate.Insert.class)
    @TableField("product_line_id")
    private Long productLineId;
    /**
     * 设备ID
     */
    @TableField("device_id")
    private Long deviceId;
    /**
     * 设备
     */
    @TableField(exist = false)
    private String deviceTitle;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 是否删除 0否 1是
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
    /**
     * 选择人员ID列表
     */
    @TableField(exist = false)
    private List<Long> userIds;
    /**
     * 不良模式ID列表
     */
    @TableField(exist = false)
    private List<Long> badModeIds;

}

