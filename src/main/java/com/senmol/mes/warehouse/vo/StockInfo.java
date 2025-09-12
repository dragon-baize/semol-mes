package com.senmol.mes.warehouse.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class StockInfo implements Serializable {
    private static final long serialVersionUID = -4041479877539611814L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String title;
    /**
     * 编号
     */
    private String code;
    /**
     * 存货量
     */
    private BigDecimal qty;
    /**
     * 仓库标识 0-成品 1-半成品 2-原料 3-非原料
     */
    private Integer sign;
    /**
     * 是否保留品 0-否 1-是
     */
    private Integer type;
    /**
     * 备注
     */
    private String remarks;
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
     * 物品列表
     */
    private List<StorageVo> storageVos;
}
