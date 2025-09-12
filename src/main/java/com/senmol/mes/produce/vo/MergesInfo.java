package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class MergesInfo implements Serializable {
    private static final long serialVersionUID = -2959751792033590493L;
    /**
     * 工单编号
     */
    private String code;
    /**
     * 计划ID
     */
    private Long planId;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 工位编号
     */
    private String stationCode;
    /**
     * 工位
     */
    private String stationTitle;
    /**
     * 工序ID
     */
    private Long id;
    /**
     * 工序编号
     */
    private Integer processCode;
    /**
     * 工序
     */
    private String processTitle;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备
     */
    private String deviceTitle;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 良品数量
     */
    private BigDecimal nonDefective;
    /**
     * 不良品数量
     */
    private BigDecimal defective;
    /**
     * 开工时间
     */
    private LocalDateTime beginTime;
    /**
     * 报工时间
     */
    private LocalDateTime endTime;
    /**
     * 良率
     */
    private BigDecimal yield;
    /**
     * 状态
     */
    private Integer status;

}
