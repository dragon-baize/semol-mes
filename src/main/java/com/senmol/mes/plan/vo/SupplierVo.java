package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class SupplierVo implements Serializable {
    private static final long serialVersionUID = 3447458849606686775L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 开户银行
     */
    private String bank;
    /**
     * 开户银行账号
     */
    private String bankAccount;
    /**
     * 付款期限
     */
    private String payment;
    /**
     * 月结日期
     */
    private Integer monthly;
    /**
     * 结算单位
     */
    private String settleCompany;
    /**
     * 预设采购价
     */
    private String estimate;
}
