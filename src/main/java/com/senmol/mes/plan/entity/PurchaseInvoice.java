package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购单开票(PurchaseInvoice)表实体类
 *
 * @author makejava
 * @since 2024-05-23 16:15:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_purchase_invoice")
public class PurchaseInvoice extends Model<PurchaseInvoice> {
    private static final long serialVersionUID = -4221818183945850783L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 制单人
     */
    @NotNull(message = "制单人不能为空", groups = ParamsValidate.Insert.class)
    @TableField("user_id")
    private Long userId;
    /**
     * 录单日期
     */
    @NotNull(message = "录单日期不能为空", groups = ParamsValidate.Insert.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("record_date")
    private LocalDate recordDate;
    /**
     * 单据编号
     */
    @TableField(value = "code", updateStrategy = FieldStrategy.NEVER)
    private String code;
    /**
     * 发票号
     */
    @NotBlank(message = "发票号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("invoice_no")
    private String invoiceNo;
    /**
     * 摘要
     */
    @TableField("digest")
    private String digest;
    /**
     * 入库总数量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 含税价
     */
    @TableField("tax_price")
    private BigDecimal taxPrice;
    /**
     * 价税总合计
     */
    @TableField("total")
    private BigDecimal total;
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
     * 经办人ID
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
     * 明细数据
     */
    @TableField(exist = false)
    private List<PurchaseInvoiceMx> mxs;

}

