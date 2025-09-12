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
 * MRP产品计算结果(PlanMrpProduct)表实体类
 *
 * @author makejava
 * @since 2023-11-14 13:09:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_mrp_product")
public class MrpProduct extends Model<MrpProduct> {
    private static final long serialVersionUID = 2997883685552673020L;
    /**
     * MRPID
     */
    @TableField("mrp_id")
    private Long mrpId;
    /**
     * 产品ID
     */
    @TableField("id")
    private Long id;
    /**
     * 产品编号
     */
    @TableField("code")
    private String code;
    /**
     * 产品名称
     */
    @TableField("title")
    private String title;
    /**
     * 生产模式 0-自制 1-委外 2-外购
     */
    @TableField("product_mode")
    private Integer productMode;
    /**
     * 毛需求量
     */
    @TableField("mao_xu_qiu_liang")
    private BigDecimal maoXuQiuLiang;
    /**
     * 生产周期
     */
    @TableField("product_cycle")
    private BigDecimal productCycle;
    /**
     * 预占用库存
     */
    @TableField("pre_inventory")
    private BigDecimal preInventory;
    /**
     * 委外周期
     */
    @TableField("outsource_cycle")
    private BigDecimal outsourceCycle;
    /**
     * 当前库存量
     */
    @TableField("finish_product")
    private BigDecimal finishProduct;
    /**
     * 销售未完成订单量
     */
    @TableField("sale_order_not")
    private BigDecimal saleOrderNot;
    /**
     * 在制生产量
     */
    @TableField("zai_zhi_liang")
    private BigDecimal zaiZhiLiang;
    /**
     * 在制委外量
     */
    @TableField("zai_zhi_wei_wai_liang")
    private BigDecimal zaiZhiWeiWaiLiang;
    /**
     * 库存可用量
     */
    @TableField("ku_cun_ke_yong_liang")
    private BigDecimal kuCunKeYongLiang;
    /**
     * 建议生产量
     */
    @TableField("jian_yi_sheng_chan_liang")
    private BigDecimal jianYiShengChanLiang;
    /**
     * 建议委外量
     */
    @TableField("jian_yi_wei_wai_liang")
    private BigDecimal jianYiWeiWaiLiang;
    /**
     * 实际生产量
     */
    @TableField("shi_ji_sheng_chan_liang")
    private BigDecimal shiJiShengChanLiang;
    /**
     * 损耗
     */
    @TableField("waste")
    private BigDecimal waste;
    /**
     * 建议生产/委外时间
     */
    @TableField("expect_date")
    private LocalDate expectDate;
    /**
     * 产品良率/%
     */
    @TableField("yield")
    private BigDecimal yield;
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

