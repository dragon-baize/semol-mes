package com.senmol.mes.warehouse.vo;

import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class RetrievalInfo implements Serializable {
    private static final long serialVersionUID = 4622002374425565868L;
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
     * 数据类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 出库明细列表
     */
    private List<RetrievalMxEntity> mxs;
}
