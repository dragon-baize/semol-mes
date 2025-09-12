package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.PageUtil;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.entity.PurchaseOrderEntity;
import com.senmol.mes.plan.service.OutsourceService;
import com.senmol.mes.plan.service.PurchaseOrderService;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.ReceiptEntity;
import com.senmol.mes.warehouse.mapper.ReceiptMapper;
import com.senmol.mes.warehouse.service.ReceiptService;
import com.senmol.mes.warehouse.vo.ReceiptVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收货管理(Receipt)表服务实现类
 *
 * @author makejava
 * @since 2023-02-13 11:02:36
 */
@Service("receiptService")
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMapper, ReceiptEntity> implements ReceiptService {

    @Lazy
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StorageInspectService storageInspectService;
    @Resource
    private OutsourceService outsourceService;
    @Resource
    private PurchaseOrderService purchaseOrderService;

    @Override
    public SaResult selectAll(PageUtil<ReceiptVo> page, ReceiptEntity receipt) {
//        if (page.getIsExport() == 1) {
//            page.checkDate(page.getStartTime(), page.getEndTime());
//        }

        List<ReceiptVo> list = this.baseMapper.selectAll(page, receipt);
        for (ReceiptVo vo : list) {
            // 字典
            vo.setUnitTitle(this.sysFromRedis.getDictMx(vo.getUnitId()));
        }

        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public SaResult getByPlanOrderNo(String planOrderNo) {
        return SaResult.data(null);
    }

    @Override
    public SaResult insertReceipt(ReceiptEntity receipt) {
        List<ReceiptEntity> list = this.lambdaQuery().eq(ReceiptEntity::getBatchNo,
                receipt.getBatchNo()).list();
        if (list.size() > 0) {
            return SaResult.error("批次号重复");
        }

        BigDecimal count = this.baseMapper.countPlanQty(receipt.getPlanOrderNo());
        receipt.setCurrentQty(receipt.getPlanQty().add(count));

        if (receipt.getType() != 3) {
            StorageInspectEntity storageInspect = new StorageInspectEntity();
            storageInspect.setReceiptCode(receipt.getPlanOrderNo());
            storageInspect.setBatchNo(receipt.getBatchNo());
            storageInspect.setCensorshipQty(receipt.getPlanQty());
            storageInspect.setGoodsId(receipt.getGoodsId());
            storageInspect.setType(receipt.getType());
            storageInspect.setSource(0);
            this.storageInspectService.insertStorageInspect(storageInspect);
        }

        this.save(receipt);

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateReceipt(ReceiptEntity receipt) {
        this.updateById(receipt);

        // 关单结束计划
        if (receipt.getIsFinish() == 1) {
            // 委外计划
            if (receipt.getStatus() == 0) {
                this.outsourceService.lambdaUpdate()
                        .set(OutsourceEntity::getStatus, 3)
                        .eq(OutsourceEntity::getCode, receipt.getPlanOrderNo())
                        .update();
            }

            // 采购单
            if (receipt.getStatus() == 0) {
                this.purchaseOrderService.lambdaUpdate()
                        .set(PurchaseOrderEntity::getStatus, 2)
                        .eq(PurchaseOrderEntity::getOrderNo, receipt.getPlanOrderNo())
                        .update();
            }
        }

        if (receipt.getIsFinish() == 0) {
            StorageInspectEntity storageInspect = new StorageInspectEntity();
            storageInspect.setReceiptCode(receipt.getPlanOrderNo());
            storageInspect.setBatchNo(receipt.getBatchNo());
            storageInspect.setCensorshipQty(receipt.getCurrentQty());
            storageInspect.setGoodsId(receipt.getGoodsId());
            storageInspect.setType(receipt.getType());
            this.storageInspectService.insertStorageInspect(storageInspect);
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public void updateStorageQty(String siCode, BigDecimal qty, LocalDateTime now, Long userId) {
        this.baseMapper.updateStorageQty(siCode, qty, now, userId);
    }

    @Override
    public SaResult delById(Long id) {
        ReceiptEntity receipt = this.getById(id);
        if (ObjUtil.isNotNull(receipt)) {
            StorageInspectEntity storageInspect = this.storageInspectService.lambdaQuery()
                    .eq(StorageInspectEntity::getBatchNo, receipt.getBatchNo())
                    .last(CheckToolUtil.LIMIT)
                    .one();

            if (storageInspect.getStatus() > 0) {
                return SaResult.error("非待检数据不允许删除");
            }

            this.storageInspectService.removeById(storageInspect.getId());
        }

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

