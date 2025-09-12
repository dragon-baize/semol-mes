package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库记录(Retrieval)表实体类
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_retrieval")
public class RetrievalEntity extends Model<RetrievalEntity> {
    private static final long serialVersionUID = 4628232608841757169L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 批次号
     */
    @TableField(value = "batch_no", condition = SqlCondition.LIKE)
    private String batchNo;
    /**
     * 出库单号(产品为发货单号，原料为工单号)
     */
    @TableField(value = "pick_order", condition = SqlCondition.LIKE)
    private String pickOrder;
    /**
     * 领料人ID
     */
    @TableField("picker")
    private Long picker;
    /**
     * 领料人ID
     */
    @TableField(exist = false)
    private String pickerName;
    /**
     * 数据类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 客户
     */
    @TableField(exist = false)
    private String custom;
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

    @TableField(exist = false)
    private List<RetrievalMxVo> mxVos;

}

