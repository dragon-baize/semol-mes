package com.senmol.mes.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用ID-名称转换工具类
 *
 * @author Administrator
 */
@Data
public class CommonPojo implements Serializable {
    private static final long serialVersionUID = -2179364081169039558L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String code;

}
