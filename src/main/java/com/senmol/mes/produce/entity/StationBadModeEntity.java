package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工位-不良模式(StationBadMode)表实体类
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_station_bad_mode")
public class StationBadModeEntity extends Model<StationBadModeEntity> {
    private static final long serialVersionUID = -4849714955683591093L;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 不良模式ID
     */
    @TableField("bad_mode_id")
    private Long badModeId;

}

