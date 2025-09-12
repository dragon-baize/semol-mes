package com.senmol.mes.workorder.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -2082952994649849619L;
    /**
     * 工单单号
     */
    private String code;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品编号
     */
    private String productCode;
    /**
     * 产品
     */
    private String productTitle;
    /**
     * 工艺ID
     */
    private Long workmanshipId;
    /**
     * 工艺
     */
    private String workmanshipTitle;
    /**
     * 工艺版本
     */
    private Integer wmsVersion;
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
}
