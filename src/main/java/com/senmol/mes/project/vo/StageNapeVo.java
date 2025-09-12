package com.senmol.mes.project.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class StageNapeVo implements Serializable {
    private static final long serialVersionUID = 1625189317843423893L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 添加项名称
     */
    private String title;
    /**
     * 责任部门
     */
    private Long deptId;
    /**
     * 选项描述
     */
    private String remarks;
    /**
     * 输入类型 0-文件 1-数据
     */
    private Integer type;
    /**
     * 是否检测 0-不审核 1-审核
     */
    private Integer isApprove;
    /**
     * 所属阶段 0-初始 1-概念 2-设计 3-工艺 4-量产
     */
    private Integer belong;
    /**
     * 是否查阅 0-未阅读 1-已阅读
     */
    private Integer isView;
    /**
     * 状态 0-没有任何文件 1-存在文件未超时未审 2-存在文件超时未审 3-正常提交
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建人
     */
    private String createUserName;

}
