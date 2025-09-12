package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.PurchaseOrderTotal;
import com.senmol.mes.plan.vo.PurchaseOrderVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-15 16:29:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderPage extends PageUtil<PurchaseOrderVo> {
    private static final long serialVersionUID = -6253848416618595327L;
    /**
     * 合计
     */
    private PurchaseOrderTotal amount;

}
