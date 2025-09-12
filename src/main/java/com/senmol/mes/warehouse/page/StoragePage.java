package com.senmol.mes.warehouse.page;

import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.warehouse.vo.StorageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dragon-baize
 * @since 2025-05-16 10:09:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StoragePage extends PageUtil<StorageVo> {
    private static final long serialVersionUID = -5226987399767455535L;
    /**
     * 合计
     */
    private StorageTotal amount;

}
