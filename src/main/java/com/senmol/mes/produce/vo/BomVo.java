package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class BomVo implements Serializable {
    private static final long serialVersionUID = -2280511910805424304L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品类型
     */
    private Integer productType;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 工艺ID
     */
    private Long workmanshipId;
    /**
     * 工艺版本
     */
    private Integer wmsVersion;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 清单物料
     */
    private List<BomMaterialVo> materialVos;

}
