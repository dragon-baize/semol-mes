package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MRP物料计算结果(PlanMrpMaterial)表实体类
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp_material")
public class MrpMaterial extends Model<MrpMaterial> {
    private static final long serialVersionUID = -1333892209469083335L;
    /**
     * MRPID
     */
    @TableField("mrp_id")
    private Long mrpId;
    /**
     * 物料ID
     */
    @TableField("id")
    private Long id;
    /**
     * 物料编号
     */
    @TableField("code")
    private String code;
    /**
     * 物料名称
     */
    @TableField("title")
    private String title;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long pId;
    /**
     * 产品编号
     */
    @TableField("p_codes")
    private String pCodes;
    /**
     * 物料单位ID
     */
    @TableField("unit_id")
    private Long unitId;
    /**
     * 库存可用量
     */
    @TableField("ku_cun_ke_yong_liang")
    private BigDecimal kuCunKeYongLiang;
    /**
     * 实际需求量
     */
    @TableField("shi_ji_xu_qiu_liang")
    private BigDecimal shiJiXuQiuLiang;
    /**
     * 销售未完成订单量
     */
    @TableField("sale_order_not")
    private BigDecimal saleOrderNot;
    /**
     * 预占用库存
     */
    @TableField("pre_inventory")
    private BigDecimal preInventory;
    /**
     * 良率损耗量
     */
    @TableField("liang_lv_sun_hao_liang")
    private BigDecimal liangLvSunHaoLiang;
    /**
     * 安全库存
     */
    @TableField("threshold_qty")
    private BigDecimal thresholdQty;
    /**
     * 当前库存量
     */
    @TableField("inventory")
    private BigDecimal inventory;
    /**
     * 采购在订量
     */
    @TableField("purchase_order_total")
    private BigDecimal purchaseOrderTotal;
    /**
     * 在制生产量
     */
    @TableField("zai_zhi_liang")
    private BigDecimal zaiZhiLiang;
    /**
     * 单个产品需求量
     */
    @TableField("single_qty")
    private BigDecimal singleQty;
    /**
     * 建议采购量
     */
    @TableField("jian_yi_cai_gou_liang")
    private BigDecimal jianYiCaiGouLiang;
    /**
     * 采购周期/天
     */
    @TableField("purchase_cycle")
    private BigDecimal purchaseCycle;
    /**
     * 需求时间
     */
    @TableField("require_time")
    private LocalDate requireTime;
    /**
     * 建议采购时间
     */
    @TableField("jian_yi_cai_gou_time")
    private LocalDate jianYiCaiGouTime;
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

}

