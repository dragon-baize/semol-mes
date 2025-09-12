package com.senmol.mes.plan.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class PurchaseInvoiceVo implements Serializable {
    private static final long serialVersionUID = 8622633815329595419L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 采购单ID
     */
    private String pids;
    /**
     * 采购单编号
     */
    private String orderNo;
    /**
     * 采购单名称
     */
    private String title;
    /**
     * 开票人
     */
    private Long userId;
    /**
     * 开票人
     */
    private String userName;
    /**
     * 供货单位
     */
    private Long supplierId;
    /**
     * 供货单位
     */
    @JsonIgnore
    private String supplierIds;
    /**
     * 供货单位
     */
    private String supplierName;
    /**
     * 录单日期
     */
    private LocalDate recordDate;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 发票号
     */
    private String invoiceNo;
    /**
     * 摘要
     */
    private String digest;
    /**
     * 存货ID
     */
    private Long goodsId;
    /**
     * 类型
     */
    private String types;
    /**
     * 存货ID
     */
    @JsonIgnore
    private String goodsIds;
    /**
     * 存货名称
     */
    private String goodsTitle;
    /**
     * 存货编号
     */
    private String goodsCode;
    /**
     * 入库总数量
     */
    private BigDecimal qty;
    /**
     * 含税价
     */
    private String taxPrice;
    /**
     * 总加税合计
     */
    private BigDecimal total;
    /**
     * 经办时间
     */
    private LocalDateTime createTime;
    /**
     * 经办人ID
     */
    private Long createUser;
    /**
     * 经办人ID
     */
    private String createUserName;
    /**
     * 版本号
     */
    private Integer version;

}
