package com.senmol.mes.plan.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author dragon-baize
 * @since 2025-08-05 15:08:41
 */
@Data
public class SaleInvoiceVo implements Serializable {
    private static final long serialVersionUID = -784569401947632673L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 销售单号
     */
    private String saleCodes;

    /**
     * 经办人
     */
    private Long agent;
    /**
     * 经办人
     */
    private String agentName;
    /**
     * 录单日期
     */
    private LocalDate registerTime;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 摘要
     */
    private String digest;
    /**
     * 存货ID
     */
    private String goodsId;
    /**
     * 存货ID列表
     */
    @JsonIgnore
    private String goodsIds;
    /**
     * 存货编号
     */
    private String goodsCode;
    /**
     * 存货名称
     */
    private String goodsTitle;
    /**
     * 类型
     */
    private String type;
    /**
     * 总数量
     */
    private BigDecimal qty;
    /**
     * 含税价
     */
    private String taxPrice;
    /**
     * 总价税合计
     */
    private BigDecimal total;
    /**
     * 开票人
     */
    private Long createUser;
    /**
     * 开票人
     */
    private String createUserName;

}
