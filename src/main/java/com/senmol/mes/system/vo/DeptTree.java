package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门树
 *
 * @author Administrator
 */
@Data
public class DeptTree implements Serializable {
    private static final long serialVersionUID = -5064992219097635008L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * 名称
     */
    private String title;
    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
    /**
     * 子级
     */
    private List<DeptTree> children = new ArrayList<>();
}
