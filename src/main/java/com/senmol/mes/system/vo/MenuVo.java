package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class MenuVo implements Serializable {
    private static final long serialVersionUID = -2415050762532919154L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * 路径
     */
    private String path;
    /**
     * 名称
     */
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
    /**
     * 类型 0-目录 1-菜单
     */
    private Integer type;
    /**
     * 状态 1-禁用 0-正常
     */
    private Integer disabled;
    /**
     * 不缓存 1-是 2-否
     */
    private Integer cached;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remarks;

}
