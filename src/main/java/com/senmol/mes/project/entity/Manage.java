package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目管理(Manage)表实体类
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_manage")
public class Manage extends Model<Manage> {
    private static final long serialVersionUID = 5415087216699118799L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField(value = "title", condition = SqlCondition.LIKE)
    private String title;
    /**
     * 等级
     */
    @NotBlank(message = "项目等级不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("level")
    private String level;
    /**
     * 报价￥(万)
     */
    @TableField("quote")
    private BigDecimal quote;
    /**
     * 产品ID
     */
    @NotNull(message = "请选择产品", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("product_id")
    private Long productId;
    /**
     * 产品
     */
    @TableField(exist = false)
    private String productTitle;
    /**
     * 产品预测数量
     */
    @NotNull(message = "产品预测数量不能为空", groups = {ParamsValidate.Insert.class, ParamsValidate.Update.class})
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 项目节点时间
     */
    @TableField("node_time")
    private LocalDateTime nodeTime;
    /**
     * 初始阶段节点时间
     */
    @TableField("init_time")
    private LocalDateTime initTime;
    /**
     * 概念阶段节点时间
     */
    @TableField("concept_time")
    private LocalDateTime conceptTime;
    /**
     * 设计阶段节点时间
     */
    @TableField("devise_time")
    private LocalDateTime deviseTime;
    /**
     * 工艺阶段节点时间
     */
    @TableField("workmanship_time")
    private LocalDateTime workmanshipTime;
    /**
     * 量产阶段节点时间
     */
    @TableField("production_time")
    private LocalDateTime productionTime;
    /**
     * 审核状态 0-待审 1-通过 2-不通过
     */
    @TableField("approve_result")
    private Integer approveResult;
    /**
     * 项目背景描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 审批备注
     */
    @TableField("notes")
    private String notes;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
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
    @JsonIgnore
    @Version
    @TableField("version")
    private Integer version;
    /**
     * 审核员ID列表
     */
    @TableField(exist = false)
    private List<Long> userIds;
    /**
     * 添加项ID列表
     */
    @TableField(exist = false)
    private List<Long> napeIds;
    /**
     * 项目人员ID列表
     */
    @TableField(exist = false)
    private List<Long> staffIds;

}

