package com.senmol.mes.quality.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class StorageInspectVo implements Serializable {
    private static final long serialVersionUID = -9058474118377282391L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 检测号
     */
    private String code;
    /**
     * 计划\工单编号父级ID
     */
    private Long pid;
    /**
     * 计划\工单编号
     */
    private String receiptCode;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 检测方式 0-抽检 1-全检
     */
    private Integer detectionWay;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    private String goodsCode;
    /**
     * 物品
     */
    private String goodsTitle;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位
     */
    private String unitTitle;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer type;
    /**
     * 送检数量
     */
    private BigDecimal censorshipQty;
    /**
     * 验货数量
     */
    private BigDecimal inspectQty;
    /**
     * 合格数量
     */
    private BigDecimal qualifiedQty;
    /**
     * 不合格数量
     */
    private BigDecimal unqualifiedQty;
    /**
     * 处理方式 0-正常入库 1-保留品入库 2-退回
     */
    private Integer disposal;
    /**
     * 状态 0-待检 1-已检 2-入库
     */
    private Integer status;
    /**
     * 数据来源 0-收货、工单 1-新增
     */
    private Integer source;
    /**
     * 检测人员ID
     */
    private Long tester;
    /**
     * 检测人员
     */
    private String testerName;
    /**
     * 描述
     */
    private String testResult;
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
