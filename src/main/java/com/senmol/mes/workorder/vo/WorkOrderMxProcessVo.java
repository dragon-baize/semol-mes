package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class WorkOrderMxProcessVo implements Serializable {
    private static final long serialVersionUID = -2147432344191818753L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 明细ID
     */
    private Long mxId;
    /**
     * 工位ID
     */
    private Long stationId;
    /**
     * 工位编号
     */
    private String stationCode;
    /**
     * 工位名称
     */
    private String stationTitle;
    /**
     * 设备
     */
    private Long deviceId;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceTitle;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 工序名称
     */
    private String processTitle;
    /**
     * 工序序号
     */
    private Integer serialNo;
    /**
     * 良品数量
     */
    private BigDecimal nonDefective;
    /**
     * 不良品数量
     */
    private BigDecimal defective;
    /**
     * 返工数量
     */
    private BigDecimal rework;
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
     * 状态 0-为报工 1-已报工
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 操作人员
     */
    private String opUsers;

}
