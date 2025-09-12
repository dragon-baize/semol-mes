package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class DictMxVo implements Serializable {
    private static final long serialVersionUID = -945288703760181313L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
}
