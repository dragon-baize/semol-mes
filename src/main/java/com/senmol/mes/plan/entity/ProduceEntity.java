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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产计划(Produce)表实体类
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_produce")
public class ProduceEntity extends Model<ProduceEntity> {
    private static final long serialVersionUID = 6589008830760172887L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @TableField("code")
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "计划名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("title")
    private String title;
    /**
     * 产线ID
     */
    @TableField(exist = false)
    private Long productLineId;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 客户名称
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 产品ID
     */
    @NotNull(message = "请选择产品", groups = ParamsValidate.Insert.class)
    @TableField("product_id")
    private Long productId;
    /**
     * 产品编号
     */
    @TableField(exist = false)
    private String productCode;
    /**
     * 产品数量
     */
    @NotNull(message = "产品数量不能为空", groups = ParamsValidate.Insert.class)
    @TableField("product_qty")
    private BigDecimal productQty;
    /**
     * 已入库数量
     */
    @TableField("rec_qty")
    private BigDecimal recQty;
    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private LocalDate deliveryDate;
    /**
     * 建议生产时间
     */
    @TableField("expect_date")
    private LocalDate expectDate;
    /**
     * 预计生产周期/H
     */
    @TableField("expect_take_time")
    private BigDecimal expectTakeTime;
    /**
     * 需求完成时间
     */
    @TableField("reality_finish_time")
    private LocalDate realityFinishTime;
    /**
     * 订单图片
     */
    @TableField("order_img")
    private String orderImg;
    /**
     * 状态 0-待生产 1-生产中 2-完成
     */
    @TableField("status")
    private Integer status;
    /**
     * 产品是否存在 0-否 1-是
     */
    @TableField("exist")
    private Integer exist;
    /**
     * 是否MRP导入 0-是 1-否
     */
    @TableField("mrp")
    private Integer mrp;
    /**
     * mrp_id
     */
    @TableField(exist = false)
    private Long mrpId;
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
     * 是否释放 0-未释放 1-释放 2-释放中
     */
    @TableField("is_free")
    private Integer isFree;
    /**
     * 工单总数
     */
    @TableField("total")
    private Integer total;
    /**
     * 已打印工单数
     */
    @TableField("printed")
    private Integer printed;
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

