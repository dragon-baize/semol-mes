package com.senmol.mes.warehouse.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-08-08 15:35:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SummaryPage extends Page<Summary> {
    private static final long serialVersionUID = 3993075805653566732L;
    /**
     * 合计
     */
    private SummaryTotal amount;

}
