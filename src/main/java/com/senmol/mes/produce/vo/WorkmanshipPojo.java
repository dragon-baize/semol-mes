package com.senmol.mes.produce.vo;

import com.senmol.mes.produce.entity.ProcessEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dragon-baize
 * @since 2025-09-08 16:15:24
 */
@Data
public class WorkmanshipPojo implements Serializable {
    private static final long serialVersionUID = 9136539943291284426L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
    /**
     * 包装ID
     */
    private Long packId;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 产线
     */
    private String productLineCode;
    /**
     * 产线
     */
    private String productLineTitle;
    /**
     * 说明文件
     */
    private String notes;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 工艺版本号
     */
    private Integer wmsVersion;
    /**
     * 工艺版本号列表
     */
    private String wmsVersions;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 工序ID列表
     */
    private List<ProcessEntity> processes;

}
