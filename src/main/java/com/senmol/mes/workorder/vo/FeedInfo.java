package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
public class FeedInfo implements Serializable {
    private static final long serialVersionUID = 395051435107564635L;

    private Long id;

    private String qrCode;

    private String batchNo;

    private BigDecimal qty;
}
