package com.senmol.mes.produce.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class WorkmanshipVo implements Serializable {
    private static final long serialVersionUID = 963982295255035175L;
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
     * 工序ID字符串
     */
    @JsonIgnore
    private String strIds;
    /**
     * 工序ID列表
     */
    private List<Long> processIds;

}
