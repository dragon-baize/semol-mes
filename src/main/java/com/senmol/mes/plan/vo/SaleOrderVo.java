package com.senmol.mes.plan.vo;

import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class SaleOrderVo implements Serializable {
    private static final long serialVersionUID = -8419980622967349990L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 类型 0-销售 1-备货
     */
    private Integer type;
    /**
     * 客户型号
     */
    private Long customId;
    /**
     * 客户型号
     */
    private String customTitle;
    /**
     * 交货日期
     */
    private LocalDate deliveryDate;
    /**
     * 发货标识 0-未发货 1-部分发货 2-已发货
     */
    private Integer sign;
    /**
     * 状态 0-未MRP 1-已MRP 2-已完成
     */
    private Integer status;
    /**
     * 开票状态 0-未完成 1-已完成
     */
    private Integer state;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 产品列表
     */
    private List<SaleOrderProductEntity> products;

}
