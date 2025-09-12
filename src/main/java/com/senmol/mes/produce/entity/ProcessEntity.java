package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 工序管理(Process)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_process")
public class ProcessEntity extends Model<ProcessEntity> {
    private static final long serialVersionUID = -2594260450130576505L;
    /**
     * 主键
     */
    @NotNull(message = "缺少主键", groups = ParamsValidate.Update.class)
    @TableId("id")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "工序名称不能为空", groups = ParamsValidate.Insert.class)
    @TableField("title")
    private String title;
    /**
     * 工艺ID
     */
    @TableField("workmanship_id")
    private Long workmanshipId;
    /**
     * 工艺版本号
     */
    @TableField("wms_version")
    private Integer wmsVersion;
    /**
     * 耗时/S
     */
    @TableField("take_time")
    private Integer takeTime;
    /**
     * 序号
     */
    @TableField("serial_no")
    private Integer serialNo;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 工位
     */
    @TableField(exist = false)
    private String stationTitle;
    /**
     * 设备ID
     */
    @TableField(exist = false)
    private Long deviceId;
    /**
     * 设备
     */
    @TableField(exist = false)
    private String deviceTitle;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField("create_user")
    private Long createUser;

}

