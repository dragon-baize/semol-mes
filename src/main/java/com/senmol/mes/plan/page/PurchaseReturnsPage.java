package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.entity.PurchaseReturns;
import com.senmol.mes.plan.vo.PurchaseReturnsTotal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 11:28:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseReturnsPage extends PageUtil<PurchaseReturns> {
    private static final long serialVersionUID = -5864198721118554332L;
    /**
     * 合计
     */
    private PurchaseReturnsTotal amount;

}
