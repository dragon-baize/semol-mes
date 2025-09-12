package com.senmol.mes.workflow.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.workflow.entity.FlowRecordUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class FlowRecordInfo implements Serializable {
    private static final long serialVersionUID = -5175121191457601921L;
    /**
     * 主键
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
     * 所属表
     */
    private String tableName;
    /**
     * 流程ID
     */
    private Long flowId;
    /**
     * 流程
     */
    private String flowTitle;
    /**
     * 审批类型ID
     */
    private Long type;
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
    /**
     * 审批信息列表字符串形式
     */
    @JsonIgnore
    private String userApprove;
    /**
     * 审批信息列表
     */
    private List<FlowRecordUser> recordUsers;

}
