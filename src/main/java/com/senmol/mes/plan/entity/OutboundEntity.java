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
 * 出库单(Outbound)表实体类
 *
 * @author makejava
 * @since 2023-03-13 10:21:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_outbound")
public class OutboundEntity extends Model<OutboundEntity> {
    private static final long serialVersionUID = 1614676791304185395L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @NotBlank(message = "出库编号不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("code")
    private String code;
    /**
     * 关联单号父级ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 关联单号-工单/委外计划/销售发货单
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 出库类型 0-生产 1-委外 2-成品 3-其他 10-发货单 20-采购退货单
     */
    @TableField("type")
    private Integer type;
    /**
     * 产品编号
     */
    @TableField("product_code")
    private String productCode;
    /**
     * 计划数量
     */
    @TableField("plan_qty")
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 状态 0-未打印 1-打印完成 2-已出库 3-部分发货
     */
    @TableField("status")
    private Integer status;
    /**
     * 建议出库时间
     */
    @TableField("advice_out_time")
    private LocalDate adviceOutTime;
    /**
     * 销售开票ID
     */
    @TableField("invoice")
    private Long invoice;
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

