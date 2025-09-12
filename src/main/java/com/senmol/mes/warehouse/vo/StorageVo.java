package com.senmol.mes.warehouse.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.senmol.mes.common.utils.ParamsValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class StorageVo implements Serializable {
    private static final long serialVersionUID = -3368509513226589016L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    @NotNull(message = "批次号不能为空", groups = ParamsValidate.Insert.class)
    private String batchNo;
    /**
     * 检测号
     */
    private String siCode;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品编号
     */
    @ExcelProperty(value = "物料/产品编号")
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
    @ExcelProperty(value = "类型 0-成品 1-半成品 2-原料 3-非原料")
    private Integer type;
    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private BigDecimal qty;
    /**
     * 剩余量
     */
    private BigDecimal residueQty;
    /**
     * 状态 0-过期 1-未过期 2-将过期
     */
    private Integer status;
    /**
     * 寿命类型 0-手动维护 1-自动维护
     */
    private Integer lifeType;
    /**
     * 寿命信息/天
     */
    private Integer lifeInfo;
    /**
     * 库位ID
     */
    private Long stockId;
    /**
     * 库位编号
     */
    @ExcelProperty(value = "库位编号")
    private String stockCode;
    /**
     * 库位
     */
    private String stockTitle;
    /**
     * 数据来源 0-系统录入 1-手工录入 2-excel导入
     */
    private Integer source;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
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
