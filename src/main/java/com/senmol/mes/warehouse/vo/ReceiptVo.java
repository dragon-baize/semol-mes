package com.senmol.mes.warehouse.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class ReceiptVo implements Serializable {
    private static final long serialVersionUID = -592021587825178187L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 计划编号
     */
    private String planOrderNo;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    private String goodsCode;
    /**
     * 物品
     */
    private String goodsTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 计划收货数量
     */
    private BigDecimal planQty;
    /**
     * 入库数量
     */
    private BigDecimal storageQty;
    /**
     * 本次收货数量
     */
    private BigDecimal currentQty;
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
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 类别 0-委外计划 1-采购单 2-客供货 3-其他
     */
    private Integer sign;
    /**
     * 状态 0-等待收货 1-已收货 2-送检
     */
    private Integer status;
    /**
     * 是否已完成 0-否 1-是
     */
    private Integer isFinish;
    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建人
     */
    private String createUserName;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
}
