package com.senmol.mes.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 项目添加项(ManageNape)表实体类
 *
 * @author makejava
 * @since 2023-03-22 10:23:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("project_manage_nape")
public class ManageNape extends Model<ManageNape> {
    private static final long serialVersionUID = -5502182289721793644L;
    /**
     * 项目ID
     */
    @TableField("manage_id")
    private Long manageId;
    /**
     * 添加项ID
     */
    @TableField("nape_id")
    private Long napeId;
    /**
     * 版本号
     */
    @JsonIgnore
    @Version
    @TableField("version")
    private Integer version;

}

