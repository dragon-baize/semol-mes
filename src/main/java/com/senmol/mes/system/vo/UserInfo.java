package com.senmol.mes.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -3421269589269136760L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 工号
     */
    private String jobNo;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 职位ID
     */
    private Long positionId;
    /**
     * 直属领导ID
     */
    private Long leaderId;

}
