package com.senmol.mes.quality.entity;

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
 * 保留品记录(QualityStorageReserva)表实体类
 *
 * @author makejava
 * @since 2023-12-20 09:32:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("quality_storage_reserva")
public class StorageReserva extends Model<StorageReserva> {
    private static final long serialVersionUID = -5741341967132316676L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 检测号
     */
    @TableField("code")
    private String code;
    /**
     * 计划\工单编号父级ID
     */
    @TableField("pid")
    private Long pid;
    /**
     * 计划\工单编号
     */
    @TableField("receipt_code")
    private String receiptCode;
    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 检测方式 0-抽检 1-全检
     */
    @TableField("detection_way")
    private Integer detectionWay;
    /**
     * 物品ID
     */
    @NotNull(message = "缺少产品主键", groups = ParamsValidate.Insert.class)
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品ID
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @NotNull(message = "缺少物品类型", groups = ParamsValidate.Insert.class)
    @TableField("type")
    private Integer type;
    /**
     * 生产模式
     */
    @TableField(exist = false)
    private Integer productMode;
    /**
     * 送检数量
     */
    @TableField("censorship_qty")
    private BigDecimal censorshipQty;
    /**
     * 验货数量
     */
    @TableField("inspect_qty")
    private BigDecimal inspectQty;
    /**
     * 合格数量
     */
    @TableField("qualified_qty")
    private BigDecimal qualifiedQty;
    /**
     * 不合格数量
     */
    @TableField("unqualified_qty")
    private BigDecimal unqualifiedQty;
    /**
     * 处理方式 2-入保留品库处理 3-保留品 4-送检 5-报废 6-退回
     */
    @TableField("disposal")
    private Integer disposal;
    /**
     * 状态 0-待处理 1-已处理
     */
    @TableField("status")
    private Integer status;
    /**
     * 数据来源 0-检测 1-新增
     */
    @TableField("source")
    private Integer source;
    /**
     * 检测人员ID
     */
    @TableField("tester")
    private Long tester;
    /**
     * 描述
     */
    @TableField("test_result")
    private String testResult;
    /**
     * 客户ID
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;
    /**
     * 退货类型 1-退货 2-调价
     */
    @TableField("return_type")
    private Integer returnType;
    /**
     * 原单价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 销售开票ID
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

