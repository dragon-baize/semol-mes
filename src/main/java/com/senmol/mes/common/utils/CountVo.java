package com.senmol.mes.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountVo implements Serializable {
    private static final long serialVersionUID = -6831150322392454462L;

    private Long aId;

    private Long bId;

    private String aStr;

    private BigDecimal qty;

}
