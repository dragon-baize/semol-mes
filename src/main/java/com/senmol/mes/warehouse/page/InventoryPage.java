package com.senmol.mes.warehouse.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.warehouse.vo.Inventory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-09-10 11:12:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryPage  extends PageUtil<Inventory> {
    private static final long serialVersionUID = -5501555081457427890L;
    /**
     * 合计
     */
    private BigDecimal amount;

}
