package com.senmol.mes.project.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.project.entity.FileUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class NapeFileInfo implements Serializable {
    private static final long serialVersionUID = -8139917619267678195L;
    /**
     * 项目ID
     */
    private Long manageId;
    /**
     * 项目名称
     */
    private String manageTitle;
    /**
     * 项目阶段
     */
    private Integer napeBelong;
    /**
     * 添加项ID
     */
    private Long napeId;
    /**
     * 添加项
     */
    private String napeTitle;
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
    private List<FileUser> fileUsers;

}
