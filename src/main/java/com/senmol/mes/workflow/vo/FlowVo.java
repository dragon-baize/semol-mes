package com.senmol.mes.workflow.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class FlowVo implements Serializable {
    private static final long serialVersionUID = -4872459285209537412L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 审批类型ID
     */
    @TableField("type")
    private Long type;

}
