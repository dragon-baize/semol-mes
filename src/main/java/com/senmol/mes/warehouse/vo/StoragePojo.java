package com.senmol.mes.warehouse.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-08-21 13:56:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoragePojo implements Serializable {
    private static final long serialVersionUID = -4199692774024563088L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 检测号
     */
    private String siCode;
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
     * 剩余量
     */
    private BigDecimal residueQty;
    /**
     * 剩余出库数量
     */
    private BigDecimal remainderQty;


}
