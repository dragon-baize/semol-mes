package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class ProcessVo implements Serializable {
    private static final long serialVersionUID = -4917146736490145705L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
    /**
     * 工序ID
     */
    private Long workmanshipId;
    /**
     * 耗时/S
     */
    private Integer takeTime;
    /**
     * 序号
     */
    private Integer serialNo;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 设备ID
     */
    private Long deviceId;

}
