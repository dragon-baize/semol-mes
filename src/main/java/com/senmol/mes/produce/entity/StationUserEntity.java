package com.senmol.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工位-人员(StationUser)表实体类
 *
 * @author makejava
 * @since 2023-01-31 10:18:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("produce_station_user")
public class StationUserEntity extends Model<StationUserEntity> {
    private static final long serialVersionUID = -3417324281984608933L;
    /**
     * 工位ID
     */
    @TableField("station_id")
    private Long stationId;
    /**
     * 人员ID
     */
    @TableField("user_id")
    private Long userId;

}

