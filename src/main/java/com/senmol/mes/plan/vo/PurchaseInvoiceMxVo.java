package com.senmol.mes.plan.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * (PurchaseInvoiceMx)表实体类
 *
 * @author dragon-xiaobai
 * @since 2025-09-01 17:06:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceMxVo extends Model<PurchaseInvoiceMxVo> {
    private static final long serialVersionUID = 870033571211359211L;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 采购单ID
     */
    private Long orderId;
    /**
     * 采购单编号
     */
    private String orderNo;
    /**
     * 入库ID\退货ID
     */
    private Long mxId;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 存货ID
     */
    private Long goodsId;
    /**
     * 存货编号
     */
    private String goodsCode;
    /**
     * 存货名称
     */
    private String goodsTitle;
    /**
     * 存货类型
     */
    private Integer goodsType;
    /**
     * 入库日期
     */
    private LocalDateTime createTime;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 类型 0-收货 1-退货
     */
    private Integer type;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;

}

