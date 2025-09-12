package com.senmol.mes.plan.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.plan.entity.MrpEntity;
import com.senmol.mes.plan.vo.MrpInfo;

import java.util.List;

/**
 * MRP(Mrp)表服务接口
 *
 * @author makejava
 * @since 2023-07-15 11:18:11
 */
public interface MrpService extends IService<MrpEntity> {

    /**
     * 主键查询单条数据
     *
     * @param id  主键
     * @return 所有数据
     */
    MrpInfo selectOne(Long id);

    /**
     * 分页查询所有数据，传入主键时为通过主键查询单条数据，size=-1时不分页显示所有数据
     *
     * @param page 分页对象
     * @param mrp  查询实体
     * @return 所有数据
     */
    Page<MrpEntity> selectAll(Page<MrpEntity> page, MrpEntity mrp);

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    SaResult insertMrp(MrpInfo info);

    /**
     * 销售未完成订单量
     *
     * @return 数量
     */
    SaResult unfinishedOrder(List<Long> productIds, Long saleOrderId);

    SaResult materialQty(Long productId);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteMrp(Long id);

    SaResult unSaleOrder();

    SaResult unWorkOrder();

}

