package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class PageUser implements Serializable {
    private static final long serialVersionUID = 4313057551702512921L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 账号标识 0MES 1项目管理 2通用
     */
    private Integer type;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 性别 1男 2女
     */
    private Integer gender;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 在职状态 0离职 1在职
     */
    private Integer onJob;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 入职日期
     */
    private LocalDate entryDate;
    /**
     * 工号
     */
    private String jobNo;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 部门名称
     */
    private String deptTitle;
    /**
     * 职位ID
     */
    private Long positionId;
    /**
     * 职位
     */
    private String positionTitle;
    /**
     * 直属领导ID
     */
    private Long leaderId;
    /**
     * 直属领导
     */
    private String leaderName;
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
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;

}
