package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author Administrator
 */
@Data
public class StationInfo implements Serializable {
    private static final long serialVersionUID = 4304474835660961316L;
    /**
     * 工单明细ID
     */
    private Long mxId;
    /**
     * 工单明细编号
     */
    private String mxCode;
    /**
     * 工单生产数量
     */
    private BigDecimal qty;
    /**
     * 开工时间
     */
    private LocalDateTime beginTime;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品编号
     */
    private String productCode;

}
