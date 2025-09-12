package com.senmol.mes.plan.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.vo.Restock;
import com.senmol.mes.plan.vo.RestockTotal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 16:06:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RestockPage extends PageUtil<Restock> {
    private static final long serialVersionUID = -2494148064341346759L;
    /**
     * 合计
     */
    private RestockTotal amount;

}
