package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料清单(Bom)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_bom")
public class BomEntity extends Model<BomEntity> {
    private static final long serialVersionUID = 1078979872058552538L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 产品ID
     */
    @NotNull(message = "请选择产品", groups = ParamsValidate.Insert.class)
    @TableField("product_id")
    private Long productId;
    /**
     * 产品类型 0-半成品 1-成品
     */
    @TableField("product_type")
    private Integer productType;
    /**
     * 产品编号
     */
    @TableField(exist = false)
    private String productCode;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productTitle;
    /**
     * 产线ID
     */
    @TableField("product_line_id")
    private Long productLineId;
    /**
     * 产线
     */
    @TableField(exist = false)
    private String productLineCode;
    /**
     * 产线
     */
    @TableField(exist = false)
    private String productLineTitle;
    /**
     * 工艺ID
     */
    @TableField(value = "workmanship_id", updateStrategy = FieldStrategy.IGNORED)
    private Long workmanshipId;
    /**
     * 工艺
     */
    @TableField(exist = false)
    private String workmanshipTitle;
    /**
     * 工艺版本号
     */
    @TableField("wms_version")
    private Integer wmsVersion;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 描述
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
     * 版本描述
     */
    @TableField("version_remarks")
    private String versionRemarks;
    /**
     * 物料列表
     */
    @NotEmpty(message = "物料列表不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(exist = false)
    private List<BomMaterialEntity> materials;

}

