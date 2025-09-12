package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class CustomVo implements Serializable {
    private static final long serialVersionUID = -7065584688709015566L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 客户名称
     */
    private String title;
}
