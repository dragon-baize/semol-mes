package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class StationVo implements Serializable {
    private static final long serialVersionUID = -3418776820339296851L;
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
     * 设备ID
     */
    private Long deviceId;
    /**
     * 描述
     */
    private String remarks;

}
