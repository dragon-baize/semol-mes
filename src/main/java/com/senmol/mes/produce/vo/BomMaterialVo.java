package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class BomMaterialVo implements Serializable {
    private static final long serialVersionUID = 3704221325364958705L;
    /**
     * 物料ID
     */
    private Long materialId;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 序号
     */
    private Integer serialNo;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 类型
     */
    private Integer type;

}
