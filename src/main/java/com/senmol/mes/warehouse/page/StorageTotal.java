package com.senmol.mes.warehouse.page;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-16 10:10:31
 */
@Data
public class StorageTotal implements Serializable {
    private static final long serialVersionUID = 7358469020289947600L;
    /**
     * 入库数量合计
     */
    private BigDecimal totalQty;
    /**
     * 剩余量合计
     */
    private BigDecimal totalResidueQty;

}
