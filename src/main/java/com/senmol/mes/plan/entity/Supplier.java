package com.senmol.mes.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 供应商管理(PlanSupplier)表实体类
 *
 * @author makejava
 * @since 2024-05-16 13:27:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("plan_supplier")
public class Supplier extends Model<Supplier> {
    private static final long serialVersionUID = 3176525466460148439L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 编号
     */
    @NotBlank(message = "单位编号不能为空", groups = ParamsValidate.Insert.class)
    @TableField("code")
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "单位名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField(value = "name", condition = SqlCondition.LIKE)
    private String name;
    /**
     * 地址
     */
    @TableField("address")
    private String address;
    /**
     * 电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 联系人
     */
    @TableField("contact")
    private String contact;
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
     * 付款期限
     */
    @TableField("payment")
    private String payment;
    /**
     * 月结日期
     */
    @TableField("monthly")
    private Integer monthly;
    /**
     * 结算单位
     */
    @TableField("settle_company")
    private String settleCompany;
    /**
     * 预设采购价
     */
    @TableField("estimate")
    private String estimate;
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
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;
    /**
     * 供应商物料
     */
    @TableField(exist = false)
    private List<SupplierGoods> goods;

}

