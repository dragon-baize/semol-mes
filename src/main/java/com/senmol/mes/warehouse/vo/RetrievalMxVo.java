package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class RetrievalMxVo implements Serializable {
    private static final long serialVersionUID = 3973866975952538025L;
    /**
     * 出库ID
     */
    private Long retrievalId;
    /**
     * 入库ID
     */
    private Long storageId;
    /**
     * 入库批次
     */
    private String batchNo;
    /**
     * 入库检测号
     */
    private String siCode;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 出库物料唯一码
     */
    private String qrCode;
    /**
     * 物品ID
     */
    private String goodsCode;
    /**
     * 物品ID
     */
    private String goodsTitle;
    /**
     * 对方编码
     */
    private String cusProCode;
    /**
     * 出库量
     */
    private BigDecimal qty;
    /**
     * 需求量
     */
    private BigDecimal requireQty;
    /**
     * 库存量
     */
    private BigDecimal residueQty;
    /**
     * 使用剩余量
     */
    private BigDecimal usedQty;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 库位ID
     */
    private Long stockId;
    /**
     * 库位编号
     */
    private String stockCode;
    /**
     * 库位
     */
    private String stockTitle;
    /**
     * 行摘要
     */
    private String remarks;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
}
