package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class LineVo implements Serializable {
    private static final long serialVersionUID = 9135106676835672182L;
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
     * 描述
     */
    private String remarks;
    /**
     * 状态ID
     */
    private Long status;

}
