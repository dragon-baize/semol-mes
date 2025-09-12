package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.SaleInvoiceTotal;
import com.senmol.mes.plan.vo.SaleInvoiceVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-08-05 15:08:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleInvoicePage  extends PageUtil<SaleInvoiceVo> {
    private static final long serialVersionUID = -6660470224506250049L;
    /**
     * 合计
     */
    private SaleInvoiceTotal amount;

}
