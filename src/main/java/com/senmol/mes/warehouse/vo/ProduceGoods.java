package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class ProduceGoods implements Serializable {
    private static final long serialVersionUID = -6747090780875740135L;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    private String goodsCode;
    /**
     * 物品类型
     */
    private Integer type;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    private Integer lifeType;
    /**
     * 寿命信息
     */
    private Integer lifeInfo;

}
