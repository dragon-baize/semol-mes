package com.senmol.mes.produce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class StationInfo implements Serializable {
    private static final long serialVersionUID = -3378527285257466384L;
    /**
     * 工位主键
     */
    private Long id;
    /**
     * 工位编号
     */
    private String code;
    /**
     * 工位名称
     */
    private String title;
    /**
     * 工序主键
     */
    private Long pid;
    /**
     * 工序名称
     */
    private String pTitle;
}
