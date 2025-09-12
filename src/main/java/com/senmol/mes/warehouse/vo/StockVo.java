package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class StockVo implements Serializable {
    private static final long serialVersionUID = 6732113188945502389L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
    /**
     * 编号
     */
    private String code;
    /**
     * 仓库标识 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer sign;
    /**
     * 是否保留品 0-否 1-是
     */
    private Integer type;
    /**
     * 备注
     */
    private String remarks;
}
