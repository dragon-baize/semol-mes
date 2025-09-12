package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class RetrievalVo implements Serializable {
    private static final long serialVersionUID = 4525654768101235784L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 出库单号(产品为发货单号，原料为工单号)
     */
    private String pickOrder;
    /**
     * 领料人ID
     */
    private Long picker;
    /**
     * 领料人
     */
    private String pickerName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建人
     */
    private String createUserName;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    private Long updateUser;
}
