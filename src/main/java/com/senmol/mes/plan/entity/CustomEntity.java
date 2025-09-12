package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户管理(Custom)表实体类
 *
 * @author makejava
 * @since 2023-07-13 16:18:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_custom")
public class CustomEntity extends Model<CustomEntity> {
    private static final long serialVersionUID = -8380551134998058869L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 地址
     */
    @TableField("address")
    private String address;
    /**
     * 税号
     */
    @TableField("account")
    private String account;
    /**
     * 联系人
     */
    @TableField("contacts")
    private String contacts;
    /**
     * 开户银行
     */
    @TableField("bank")
    private String bank;
    /**
     * 开户银行账号
     */
    @TableField("bank_account")
    private String bankAccount;
    /**
     * 信用额度
     */
    @TableField("credits")
    private BigDecimal credits;
    /**
     * 收款期限
     */
    @TableField("payment")
    private String payment;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @OrderBy
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 创建人
     */
    @TableField(exist = false)
    private String createUserName;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 修改人
     */
    @TableField(exist = false)
    private String updateUserName;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;
    /**
     * 产品ID
     */
    @TableField(exist = false)
    private List<CustomProductEntity> customProducts;

}

