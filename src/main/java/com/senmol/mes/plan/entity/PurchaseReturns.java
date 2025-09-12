package com.senmol.mes.plan.entity;

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
 * 采购退货(PlanPurchaseReturns)表实体类
 *
 * @author makejava
 * @since 2024-05-17 09:29:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_purchase_returns")
public class PurchaseReturns extends Model<PurchaseReturns> {
    private static final long serialVersionUID = 1880269123683684136L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 采购单ID
     */
    @NotNull(message = "缺少采购单主键", groups = ParamsValidate.Insert.class)
    @TableField("pid")
    private Long pid;
    /**
     * 采购单编号
     */
    @NotBlank(message = "请选择采购单", groups = ParamsValidate.Insert.class)
    @TableField("code")
    private String code;
    /**
     * 物料ID
     */
    @NotNull(message = "请选择物料", groups = ParamsValidate.Insert.class)
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物料编号
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String goodsTitle;
    /**
     * 单位ID
     */
    @TableField(exist = false)
    private Long unitId;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unitTitle;
    /**
     * 物品类型 1-半成品 2-物料
     */
    @TableField("type")
    private Integer type;
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = ParamsValidate.Insert.class)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 仓库全名
     */
    @TableField("warehouse")
    private String warehouse;
    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 税率
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;
    /**
     * 摘要
     */
    @TableField("digest")
    private String digest;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 采购开票ID
     */
    @TableField("invoice")
    private Long invoice;
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

