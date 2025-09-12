package com.senmol.mes.warehouse.entity;

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
 * 库位管理(Stock)表实体类
 *
 * @author makejava
 * @since 2023-01-29 17:08:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_stock")
public class StockEntity extends Model<StockEntity> {
    private static final long serialVersionUID = -3340677672912684949L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "库位名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("title")
    private String title;
    /**
     * 编号
     */
    @NotBlank(message = "库位编号不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "code", condition = SqlCondition.LIKE)
    private String code;
    /**
     * 存货量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 仓库标识 0-成品 1-半成品 2-原料 3-非原料
     */
    @NotNull(message = "请选择库位类型", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("sign")
    private Integer sign;
    /**
     * 是否保留品 0-否 1-是
     */
    @TableField("type")
    private Integer type;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
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

