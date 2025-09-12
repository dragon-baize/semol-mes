package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class GoodsInfo implements Serializable {
    private static final long serialVersionUID = 6571942414762364608L;

    private String batchNo;

    private String siCode;

    private Long goodsId;

    private String goodsCode;

    private String goodsTitle;

    private Integer type;

    private BigDecimal residueQty;

    private Long stockId;

    private String stockCode;

    private String stockTitle;

    private String remarks;

    private LocalDateTime createTime;

    private BigDecimal qty;


}
