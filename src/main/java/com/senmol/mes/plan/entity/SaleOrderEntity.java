package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售订单(SaleOrder)表实体类
 *
 * @author makejava
 * @since 2023-03-13 13:29:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_sale_order")
public class SaleOrderEntity extends Model<SaleOrderEntity> {
    private static final long serialVersionUID = 4551160013073670071L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @TableField(value = "code", condition = SqlCondition.LIKE, updateStrategy = FieldStrategy.NEVER)
    private String code;
    /**
     * 类型 0-销售 1-备货
     */
    @TableField("type")
    private Integer type;
    /**
     * 客户型号
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 客户型号
     */
    @TableField(exist = false)
    private String customTitle;
    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private LocalDate deliveryDate;
    /**
     * 发货标识 0-未发货 1-部分发货 2-已发货
     */
    @TableField("sign")
    private Integer sign;
    /**
     * 状态 0-未MRP 1-已MRP 2-已完成
     */
    @TableField("status")
    private Integer status;
    /**
     * 开票状态 0-未完成 1-已完成
     */
    @TableField("state")
    private Integer state;
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
    /**
     * 产品列表
     */
    @TableField(exist = false)
    private List<SaleOrderProductEntity> products;

}

