package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.RequisitionVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author dragon-baize
 * @since 2025-05-15 15:08:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequisitionPage extends PageUtil<RequisitionVo> {
    private static final long serialVersionUID = -6944603966671604552L;
    /**
     * 建议采购数量合计
     */
    private BigDecimal totalAdviceQty;

}
