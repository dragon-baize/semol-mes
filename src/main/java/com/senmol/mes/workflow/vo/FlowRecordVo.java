package com.senmol.mes.workflow.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class FlowRecordVo implements Serializable {
    private static final long serialVersionUID = -5175121191457601921L;
    /**
     * 流程名称
     */
    private String title;
    /**
     * 审批ID
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long itemId;
    /**
     * 项目编号
     */
    private String itemCode;
    /**
     * 项目名称
     */
    private String itemTitle;
    /**
     * 流程ID
     */
    private Long flowId;
    /**
     * 流程
     */
    private String flowTitle;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 当前状态 0-待审 1-审核中 2-不通过 3-通过 4-退回
     */
    private Integer status;
    /**
     * 发起时间
     */
    private LocalDateTime createTime;
    /**
     * 发起人ID
     */
    private Long createUser;
    /**
     * 发起人
     */
    private String createUserName;

}
