package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 项目人员结构(ManageStaff)表实体类
 *
 * @author makejava
 * @since 2023-03-23 09:20:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_manage_staff")
public class ManageStaff extends Model<ManageStaff> {
    private static final long serialVersionUID = -5079573416834412769L;
    /**
     * 项目ID
     */
    @TableField("manage_id")
    private Long manageId;
    /**
     * 人员ID
     */
    @TableField("staff_id")
    private Long staffId;

}

