package com.senmol.mes.plan.vo;

import com.senmol.mes.plan.entity.MrpMaterial;
import com.senmol.mes.plan.entity.MrpProduct;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class MrpInfo implements Serializable {
    private static final long serialVersionUID = 1844751505204177564L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 销售订单ID
     */
    private Long saleOrderId;
    /**
     * 是否已计算 0-未保存 1-已保存 2-已释放
     */
    private Integer isCount;
    /**
     * 产品信息
     */
    private List<MrpProduct> products;
    /**
     * 物料信息
     */
    private List<MrpMaterial> materials;
}
