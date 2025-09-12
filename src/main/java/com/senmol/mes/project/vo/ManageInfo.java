package com.senmol.mes.project.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.project.entity.ManageUser;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class ManageInfo implements Serializable {
    private static final long serialVersionUID = -2432469339624804296L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 项目名称
     */
    private String title;
    /**
     * 报价￥(万)
     */
    private BigDecimal quote;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品
     */
    private String productTitle;
    /**
     * 产品预测数量
     */
    private BigDecimal qty;
    /**
     * 项目节点时间
     */
    private LocalDateTime nodeTime;
    /**
     * 审批备注
     */
    private String notes;
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
    private List<ManageUser> manageUsers;

}
