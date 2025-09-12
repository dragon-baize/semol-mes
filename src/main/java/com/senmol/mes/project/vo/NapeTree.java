package com.senmol.mes.project.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class NapeTree implements Serializable {
    private static final long serialVersionUID = 8274555686495370379L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 添加项名称
     */
    private String title;
    /**
     * 部门
     */
    private Long deptId;
    /**
     * 所属阶段 0-初始 1-概念 2-设计 3-工艺 4-量产
     */
    private Integer belong;
    /**
     * 文件数量
     */
    private Integer fileNum;
    /**
     * 子级
     */
//    private List<NapeTree> children;

}
