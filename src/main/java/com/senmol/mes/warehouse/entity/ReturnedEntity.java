package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退库记录(Returned)表实体类
 *
 * @author makejava
 * @since 2023-08-04 20:14:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_returned")
public class ReturnedEntity extends Model<ReturnedEntity> {
    private static final long serialVersionUID = 4455229438305068023L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 出库物料唯一码
     */
    @TableField("qr_code")
    private String qrCode;
    /**
     * 入库批次号
     */
    @TableField("in_batch_no")
    private String inBatchNo;
    /**
     * 出库批次号
     */
    @TableField("out_batch_no")
    private String outBatchNo;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 物品编号
     */
    @TableField(exist = false)
    private String goodsCode;
    /**
     * 物品名称
     */
    @TableField(exist = false)
    private String goodsTitle;
    /**
     * 物品类型
     */
    @TableField("type")
    private Integer type;
    /**
     * 退库数量
     */
    @TableField("return_qty")
    private BigDecimal returnQty;
    /**
     * 损耗量
     */
    @TableField("wastage")
    private BigDecimal wastage;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField("create_user")
    private Long createUser;

}

