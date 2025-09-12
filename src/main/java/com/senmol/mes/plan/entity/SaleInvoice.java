package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售发货单开票(SaleInvoice)表实体类
 *
 * @author makejava
 * @since 2024-05-23 14:28:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_sale_invoice")
public class SaleInvoice extends Model<SaleInvoice> {
    private static final long serialVersionUID = -7182511723338308956L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 制单人
     */
    @TableField("drawer")
    private Long drawer;
    /**
     * 制单人
     */
    @TableField(exist = false)
    private String drawerName;
    /**
     * 录单日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("register_time")
    private LocalDate registerTime;
    /**
     * 单据编号
     */
    @TableField("code")
    private String code;
    /**
     * 经办人ID
     */
    @TableField("agent")
    private Long agent;
    /**
     * 经办人
     */
    @TableField(exist = false)
    private String agentName;
    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;
    /**
     * 发货仓
     */
    @TableField("warehouse")
    private String warehouse;
    /**
     * 客户ID
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 应收货款
     */
    @TableField("receivables")
    private BigDecimal receivables;
    /**
     * 摘要
     */
    @TableField("digest")
    private String digest;
    /**
     * 类型 1-发货 3-调价
     */
    @TableField("type")
    private Integer type;
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
     * 明细列表
     */
    @TableField(exist = false)
    private List<SaleInvoiceMx> mxs;

}

