package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class DeliveryVo implements Serializable {
    private static final long serialVersionUID = 7749579270830105013L;
    /**
     * 是否已开票
     */
    private Integer mark;
    /**
     * 单据日期
     */
    private LocalDateTime createTime;
    /**
     * 主键
     */
    private Long id;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 出库单ID
     */
    private Long outboundId;
    /**
     * 出库单编号
     */
    private String obCode;
    /**
     * 存货ID
     */
    private Long productId;
    /**
     * 存货编号
     */
    private String productCode;
    /**
     * 存货全名
     */
    private String productTitle;
    /**
     * 客户ID
     */
    private Long customId;
    /**
     * 客户全名
     */
    private String customTitle;
    /**
     * 经办人ID
     */
    private Long createUser;
    /**
     * 经办人全名
     */
    private String createUserName;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税价
     */
    private BigDecimal taxPrice;
    /**
     * 类别 1-发货 2-退货
     */
    private Integer type;

}
