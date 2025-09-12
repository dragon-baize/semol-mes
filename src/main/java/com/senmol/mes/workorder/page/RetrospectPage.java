package com.senmol.mes.workorder.page;

import com.senmol.mes.common.utils.PageUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-08-13 10:18:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RetrospectPage extends PageUtil<Retrospect> {
    private static final long serialVersionUID = 2387460246261015711L;

    private RetrospectTotal amount;

}
