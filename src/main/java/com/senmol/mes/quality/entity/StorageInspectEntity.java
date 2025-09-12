package com.senmol.mes.quality.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库检测(StorageInspect)表实体类
 *
 * @author makejava
 * @since 2023-02-13 16:50:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("quality_storage_inspect")
public class StorageInspectEntity extends Model<StorageInspectEntity> {
    private static final long serialVersionUID = -3090781516122386983L;
    /**
     * 主键
     */
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
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品名称
     */
    @TableField(exist = false)
    private String goodsTitle;
    /**
     * 物品名称
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
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
     * 处理方式 0-正常入库 1-部分入库 2-入保留品库处理 3-保留品
     */
    @TableField("disposal")
    private Integer disposal;
    /**
     * 状态 0-待检 1-已检 2-入库 3-退回 4-复检
     */
    @TableField("status")
    private Integer status;
    /**
     * 数据来源 0-收货、工单 1-新增
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

