package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.vo.ProductInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产线管理(ProductLine)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface ProductLineService extends IService<ProductLineEntity> {

    /**
     * 查询产线对应的工艺、产品、工序、工位、设备
     *
     * @param productLineId 产线ID
     * @return 产品信息
     */
    List<ProductInfo> selectById(Long productLineId);

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param productLine 查询实体
     * @return 所有数据
     */
    Page<ProductLineEntity> selectAll(Page<ProductLineEntity> page, ProductLineEntity productLine);

    /**
     * 查人员所在产线
     *
     * @param userId 人员ID
     * @return 产线数据
     */
    List<ProductLineEntity> getByUserId(Long userId);

    /**
     * 查询工作台实际数量
     *
     * @return 实际数量
     */
    BigDecimal getAllTotal();

    /**
     * 新增数据
     *
     * @param productLine 实体对象
     * @return 新增结果
     */
    SaResult insertLine(ProductLineEntity productLine);

    /**
     * 修改数据
     *
     * @param productLine 实体对象
     * @return 修改结果
     */
    SaResult updateLine(ProductLineEntity productLine);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteLine(Long id);

}

