package com.senmol.mes.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 替换ID-名称/姓名
 *
 * @author Administrator
 */
@Data
public class ConvertVo implements Serializable {
    private static final long serialVersionUID = -8260978849204128060L;
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String title;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 编号
     */
    private String code;
    /**
     * 单位
     */
    private Long unitId;
}
