package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.DeliveryVo;
import com.senmol.mes.plan.vo.DeliveryVoTotal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 09:53:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeliveryVoPage extends PageUtil<DeliveryVo> {
    private static final long serialVersionUID = 6508033194330852691L;
    /**
     * 合计
     */
    private DeliveryVoTotal amount;

}
