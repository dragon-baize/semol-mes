package com.senmol.mes.workflow.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class MenuTrees implements Serializable {
    private static final long serialVersionUID = -8313213140350464777L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * 标题
     */
    private String title;
    /**
     * 审批是否启用 0-禁用 1-启用
     */
    private Integer status;
    /**
     * 审批是否流转 0-否 1-是
     */
    private Integer isUse;
    /**
     * 子级菜单
     */
    private List<MenuTrees> children;
}
