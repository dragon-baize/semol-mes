package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 供应商物料(SupplierMaterial)表实体类
 *
 * @author makejava
 * @since 2024-05-17 11:24:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_supplier_goods")
public class SupplierGoods extends Model<SupplierGoods> {
    private static final long serialVersionUID = -1876706333841354604L;
    /**
     * 供应商ID
     */
    @TableField("pid")
    private Long pid;
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
     * 类型
     */
    @TableField("type")
    private Integer type;
    /**
     * 客户物料编号
     */
    @NotBlank(message = "客户物料编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("cus_mat_code")
    private String cusMatCode;
    /**
     * 单价
     */
    @NotNull(message = "单价不能为空", groups = ParamsValidate.Insert.class)
    @TableField("price")
    private BigDecimal price;
    /**
     * 税率
     */
    @NotNull(message = "税率不能为空", groups = ParamsValidate.Insert.class)
    @TableField("tax_rate")
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    @NotNull(message = "含税价不能为空", groups = ParamsValidate.Insert.class)
    @TableField("tax_price")
    private BigDecimal taxPrice;

}

