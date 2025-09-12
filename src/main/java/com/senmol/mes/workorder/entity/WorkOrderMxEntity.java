package com.senmol.mes.workorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工单明细(WorkOrderMx)表实体类
 *
 * @author makejava
 * @since 2023-02-20 11:03:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_order_mx")
public class WorkOrderMxEntity extends Model<WorkOrderMxEntity> {
    private static final long serialVersionUID = 1141941161092198961L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 工单ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 工单批次号
     */
    @TableField("code")
    private String code;
    /**
     * 产品ID
     */
    @TableField(exist = false)
    private Long productId;
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
     * 工单数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 良品数量
     */
    @TableField("non_defective")
    private BigDecimal nonDefective;
    /**
     * 不良品数量
     */
    @TableField("defective")
    private BigDecimal defective;
    /**
     * 开工时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;
    /**
     * 完工时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;
    /**
     * 状态 0-未打印 1-已打印
     */
    @TableField("status")
    private Integer status;
    /**
     * 完成状态 0-未完成 1-完成
     */
    @TableField("finish")
    private Integer finish;
    /**
     * 是否释放 0-未释放 1-释放
     */
    @TableField("is_free")
    private Integer isFree;
    /**
     * 良率
     */
    @TableField("yield")
    private BigDecimal yield;
    /**
     * 是否确认
     */
    @TableField("is_confirm")
    private Integer isConfirm;
    /**
     * 是否已领取 0-否 1-是
     */
    @TableField("is_receive")
    private Integer isReceive;
    /**
     * 是否送检 0-未送检 1-送检
     */
    @TableField("submit")
    private Integer submit;
    /**
     * 是否删除 0否 1是
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
     * 创建人
     */
    @TableField(exist = false)
    private String createUserName;
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

