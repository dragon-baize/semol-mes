package com.senmol.mes.warehouse.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出库明细(RetrievalMx)表实体类
 *
 * @author makejava
 * @since 2023-07-24 16:54:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_retrieval_mx")
public class RetrievalMxEntity extends Model<RetrievalMxEntity> {
    private static final long serialVersionUID = -7417493938598624823L;
    /**
     * 出库ID
     */
    @TableField("retrieval_id")
    private Long retrievalId;
    /**
     * 入库ID
     */
    @TableField("storage_id")
    private Long storageId;
    /**
     * 物品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 出库物料唯一码
     */
    @TableField("qr_code")
    private String qrCode;
    /**
     * 物品类型 0-成品 1-半成品 2-原料 3-非原料
     */
    @TableField("type")
    private Integer type;
    /**
     * 出库量
     */
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 需求量
     */
    @TableField("require_qty")
    private BigDecimal requireQty;
    /**
     * 库存量
     */
    @TableField("residue_qty")
    private BigDecimal residueQty;
    /**
     * 使用剩余量
     */
    @TableField("used_qty")
    private BigDecimal usedQty;
    /**
     * 库位ID
     */
    @TableField("stock_id")
    private Long stockId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人ID
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 修改人ID
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

}

