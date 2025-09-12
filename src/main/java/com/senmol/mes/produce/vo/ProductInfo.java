package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = 1712765734184602185L;
    /**
     * 计划ID
     */
    private Long id;
    /**
     * 工艺ID
     */
    private Long workmanshipId;
    /**
     * 工艺
     */
    private String workmanshipTitle;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品
     */
    private String productTitle;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 工序、工位、设备信息
     */
    private List<MergesInfo> mergeInfos;
}
