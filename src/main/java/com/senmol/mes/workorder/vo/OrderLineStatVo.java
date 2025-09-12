package com.senmol.mes.workorder.vo;

import com.senmol.mes.common.utils.ParamsValidate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class OrderLineStatVo implements Serializable {
    private static final long serialVersionUID = 80969855247216671L;
    /**
     * 工单单号
     */
    @NotBlank(message = "缺少工单单号", groups = ParamsValidate.Update.class)
    private String orderCode;
    /**
     * 产线编号
     */
    @NotBlank(message = "缺少产线编号", groups = ParamsValidate.Update.class)
    private String productLineCode;
    /**
     * 工位编号
     */
    @NotBlank(message = "缺少工位编号", groups = ParamsValidate.Update.class)
    private String stationCode;
    /**
     * 工序名称
     */
    @NotBlank(message = "缺少工序名称", groups = ParamsValidate.Update.class)
    private String processTitle;
    /**
     * 状态 0-完成 1-NG 2-跳过
     */
    @NotNull(message = "缺少生产状态", groups = ParamsValidate.Update.class)
    private Integer status;
}
