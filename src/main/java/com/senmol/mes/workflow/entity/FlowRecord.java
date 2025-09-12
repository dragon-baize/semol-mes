package com.senmol.mes.workflow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审核记录(FlowRecord)表实体类
 *
 * @author makejava
 * @since 2023-03-24 14:25:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("work_flow_record")
public class FlowRecord extends Model<FlowRecord> {
    private static final long serialVersionUID = -7846460632068332123L;
    /**
     * 主键
     */
    @TableId("id")
    @TableField("id")
    private Long id;
    /**
     * 菜单ID
     */
    @TableField(exist = false)
    private String title;
    /**
     * 项目ID
     */
    @TableField("item_id")
    private Long itemId;
    /**
     * 项目编号
     */
    @TableField("item_code")
    private String itemCode;
    /**
     * 项目名称
     */
    @TableField("item_title")
    private String itemTitle;
    /**
     * 所属表
     */
    @TableField("table_name")
    private String tableName;
    /**
     * 流程名称
     */
    @TableField(exist = false)
    private String menuTitle;
    /**
     * 流程ID
     */
    @TableField("flow_id")
    private Long flowId;
    /**
     * 是否生产计划
     */
    @TableField(exist = false)
    private Integer isProductPlan;
    /**
     * 描述
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 当前状态 0-待审 1-审核中 2-不通过 3-通过
     */
    @TableField("status")
    private Integer status;
    /**
     * 是否删除 0否 NULL是
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 发起时间
     */
    private LocalDateTime createTime;
    /**
     * 发起人ID
     */
    private Long createUser;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

