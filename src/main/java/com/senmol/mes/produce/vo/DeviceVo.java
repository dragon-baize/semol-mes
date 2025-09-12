package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class DeviceVo implements Serializable {
    private static final long serialVersionUID = -7057687644832544528L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String title;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 厂商
     */
    private String manufacturer;
    /**
     * 售后人员
     */
    private String afterSalesMan;
    /**
     * 售后电话
     */
    private String afterSalesPhone;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 状态ID
     */
    private Long status;

}
