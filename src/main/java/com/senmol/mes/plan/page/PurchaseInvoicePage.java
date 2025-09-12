package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.PurchaseInvoiceTotal;
import com.senmol.mes.plan.vo.PurchaseInvoiceVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 08:58:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoicePage extends PageUtil<PurchaseInvoiceVo> {
    private static final long serialVersionUID = -8801341888862431431L;
    /**
     * 合计
     */
    private PurchaseInvoiceTotal amount;

}
