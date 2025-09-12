package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.OrderMxVo;
import com.senmol.mes.plan.vo.OrderMxVoTotal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 09:20:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderMxPage extends PageUtil<OrderMxVo> {
    private static final long serialVersionUID = 6459614526654856277L;
    /**
     * 合计
     */
    private OrderMxVoTotal amount;

}
