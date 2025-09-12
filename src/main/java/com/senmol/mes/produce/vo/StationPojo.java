package com.senmol.mes.produce.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class StationPojo implements Serializable {
    private static final long serialVersionUID = 2019612886311681624L;
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
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 产线
     */
    private String productLineTitle;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 工序
     */
    private String processTitle;

}
