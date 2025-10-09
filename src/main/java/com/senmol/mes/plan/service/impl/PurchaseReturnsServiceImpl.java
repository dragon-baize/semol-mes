package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import com.senmol.mes.plan.entity.PurchaseReturns;
import com.senmol.mes.plan.mapper.PurchaseReturnsMapper;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.service.PurchaseReturnsService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 采购退货(PlanPurchaseReturns)表服务实现类
 *
 * @author makejava
 * @since 2024-05-17 09:29:36
 */
@Service("purchaseReturnsService")
public class PurchaseReturnsServiceImpl extends ServiceImpl<PurchaseReturnsMapper, PurchaseReturns> implements PurchaseReturnsService {

    @Resource
    private OutboundService outboundService;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StorageService storageService;

    @Override
    public SaResult selectAll(Page<PurchaseReturns> page, PurchaseReturns purchaseReturns) {
        if (ObjectUtil.isNull(purchaseReturns.getType())) {
            return SaResult.error("请选择物品类型");
        }

        List<PurchaseReturns> list = this.baseMapper.selectAll(page, purchaseReturns);
        list.forEach(item -> item.setUnitTitle(this.sysFromRedis.getDictMx(item.getUnitId())));
        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public SaResult insert(PurchaseReturns purchaseReturns) {
        BigDecimal decimal = this.storageService.getReturns(purchaseReturns.getCode());
        if (decimal.compareTo(BigDecimal.ZERO) == 0) {
            return SaResult.error("采购订单未入库或无库存");
        }

        if (purchaseReturns.getQty().compareTo(decimal) > 0) {
            return SaResult.error("库存不足，库存剩余量【" + decimal.stripTrailingZeros().toPlainString() + "】");
        }

        Long total = this.lambdaQuery().eq(PurchaseReturns::getBatchNo, purchaseReturns.getBatchNo()).count();
        if (total > 0L) {
            return SaResult.error("批次号重复");
        }

        String date = LocalDate.now().toString();
        int count = this.baseMapper.getTodayCount(date);
        purchaseReturns.setOrderNo("CGTH" + date.replace("-", "") + (101 + count * 3));
        this.save(purchaseReturns);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult modify(PurchaseReturns purchaseReturns) {
        Long id = purchaseReturns.getId();
        PurchaseReturns returns = this.getById(id);
        if (returns.getStatus() == 1) {
            return SaResult.error("退货单已完成出库");
        }

        this.updateById(purchaseReturns);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public void setInvoices(List<Long> returnsIds, Long invoice) {
        this.baseMapper.setInvoices(returnsIds, invoice);
    }

    @Override
    public SaResult delete(Long id) {
        PurchaseReturns purchaseReturns = this.getById(id);
        if (purchaseReturns.getStatus() == 1) {
            return SaResult.error("退货单已完成出库");
        }

        // TODO 删除采购退货单是否删除出库单
        OutboundEntity outbound = this.outboundService.lambdaQuery()
                .eq(OutboundEntity::getPid, id)
                .last(CheckToolUtil.LIMIT)
                .one();
        if (ObjectUtil.isNotNull(outbound)) {
            Long outboundId = outbound.getId();
            this.outboundService.removeById(outboundId);
            this.outboundMxService.lambdaUpdate().eq(OutboundMxEntity::getOutboundId, outboundId).remove();
        }

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

