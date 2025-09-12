package com.senmol.mes.workorder.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class StatInfo implements Serializable {
    private static final long serialVersionUID = 2667442668805940028L;
    /**
     * 日期
     */
    private Integer date;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 工单ID
     */
    @JsonIgnore
    private String ids;
    /**
     * 生产数量
     */
    @JsonIgnore
    private BigDecimal pQty;
    /**
     * 入库数量
     */
    @JsonIgnore
    private BigDecimal sQty;
    /**
     * 良率
     */
    private BigDecimal yield;

}
