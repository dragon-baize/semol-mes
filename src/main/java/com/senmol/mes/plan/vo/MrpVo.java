package com.senmol.mes.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class MrpVo implements Serializable {
    private static final long serialVersionUID = -3545581362910053352L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 销售订单ID
     */
    private Long saleOrderId;
    /**
     * 销售订单编号
     */
    private String code;
    /**
     * 是否已计算 0-未保存 1-已保存 2-已释放
     */
    private Integer isCount;
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
