package com.senmol.mes.project.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.project.entity.NapeUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class StageNapeInfo implements Serializable {
    private static final long serialVersionUID = 884309247097271491L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 项目阶段
     */
    private Integer belong;
    /**
     * 添加项名称
     */
    private String title;
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
    /**
     * 审批信息列表字符串形式
     */
    @JsonIgnore
    private String userApprove;
    /**
     * 审批信息列表
     */
    private List<NapeUser> napeUsers;

}
