package com.senmol.mes.quality.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class BadModeVo implements Serializable {
    private static final long serialVersionUID = -6587466125588585527L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 不良方式编号
     */
    private String code;
    /**
     * 不良方式
     */
    private String title;
    /**
     * 产线ID
     */
    private Long productLineId;
    /**
     * 描述
     */
    private String remarks;

}
