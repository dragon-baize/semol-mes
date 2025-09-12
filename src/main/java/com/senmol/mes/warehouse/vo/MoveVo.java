package com.senmol.mes.warehouse.vo;

import com.senmol.mes.common.utils.ParamsValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class MoveVo implements Serializable {

    private static final long serialVersionUID = 6458355135907718896L;

    /**
     * 入库主键
     */
    @NotNull(message = "入库主键不能为空", groups = ParamsValidate.Insert.class)
    private Long id;
    /**
     * 批次号
     */
    @NotNull(message = "迁移批次号不能为空", groups = ParamsValidate.Insert.class)
    private String rBatchNo;
    /**
     * 数量
     */
    @NotNull(message = "迁移数量不能为空", groups = ParamsValidate.Insert.class)
    private BigDecimal rQty;
    /**
     * 库位主键
     */
    @NotNull(message = "请选择迁移库位", groups = ParamsValidate.Insert.class)
    private Long rStockId;
    /**
     * 备注
     */
    private String rRemarks;

}
