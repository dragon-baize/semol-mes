package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.PurchaseOrderEntity;
import com.senmol.mes.plan.entity.RequisitionEntity;
import com.senmol.mes.plan.mapper.PurchaseOrderMapper;
import com.senmol.mes.plan.page.PurchaseOrderPage;
import com.senmol.mes.plan.page.RestockPage;
import com.senmol.mes.plan.service.PurchaseOrderService;
import com.senmol.mes.plan.service.RequisitionService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.plan.vo.*;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 采购单(PurchaseOrder)表服务实现类
 *
 * @author makejava
 * @since 2023-03-13 09:25:53
 */
@Service("purchaseOrderService")
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrderEntity> implements PurchaseOrderService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private RequisitionService requisitionService;
    @Resource
    private PlanFromRedis planFromRedis;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public SaResult selectAll(PurchaseOrderPage page, PurchaseOrderEntity purchaseOrder) {
        CompletableFuture<List<PurchaseOrderVo>> selectAll = CompletableFuture.supplyAsync(() -> {
            List<PurchaseOrderVo> voList = this.baseMapper.selectAll(page, purchaseOrder);
            voList.forEach(item -> {
                SupplierVo supplierVo = this.planFromRedis.getSupplier(item.getSupplierId());
                if (ObjUtil.isNotNull(supplierVo)) {
                    item.setSupplierTitle(supplierVo.getName());
                }

                item.setUnitTitle(this.sysFromRedis.getDictMx(item.getUnitId()));
            });

            return voList;
        }, this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("采购单列表查询失败，请重试");
        });

        if (page.getSize() == -1) {
            page.setRecords(selectAll.join());
            return SaResult.data(page);
        }

        CompletableFuture<PurchaseOrderTotal> selectTotal = CompletableFuture.supplyAsync(() ->
                        this.baseMapper.selectTotal(page.getStartTime(), page.getEndTime(), purchaseOrder),
                this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("合计统计失败，请重试");
        });

        PurchaseOrderPage join = selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);

    }

    @Override
    public SaResult restock(RestockPage page, Restock restock) {
        if (ObjectUtil.isNull(restock.getType())) {
            return SaResult.error("请选择物品类型");
        }

        CompletableFuture<List<Restock>> restockAll = CompletableFuture.supplyAsync(() -> {
            List<Restock> list = this.baseMapper.restockAll(page, restock);
            list.forEach(item -> {
                SupplierVo supplierVo = this.planFromRedis.getSupplier(item.getSupplierId());
                if (ObjUtil.isNotNull(supplierVo)) {
                    item.setSupplierName(supplierVo.getName());
                }

                item.setUnitTitle(this.sysFromRedis.getDictMx(item.getUnitId()));
                item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
            });

            return list;
        }, this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("收退货列表查询失败，请重试");
        });

        CompletableFuture<RestockTotal> restockTotal = CompletableFuture.supplyAsync(() ->
                        this.baseMapper.restockTotal(page.getStartTime(), page.getEndTime(), restock),
                this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("合计统计失败，请重试");
        });

        RestockPage join = restockAll.thenCombine(restockTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public PurchaseOrderVo selectOne(Long id) {
        PurchaseOrderEntity purchaseOrder = this.getById(id);
        PurchaseOrderVo purchaseOrderVo = Convert.convert(PurchaseOrderVo.class, purchaseOrder);

        RequisitionEntity requisition = this.requisitionService.getById(purchaseOrderVo.getRequisitionId());
        purchaseOrderVo.setRequisitionCode(requisition.getCode());
        purchaseOrderVo.setRequisitionTitle(requisition.getTitle());
        if (purchaseOrderVo.getType() == 1) {
            ProductVo product = this.proFromRedis.getProduct(purchaseOrderVo.getMaterialId());
            if (ObjUtil.isNotNull(product)) {
                purchaseOrderVo.setMaterialCode(product.getCode());
                purchaseOrderVo.setMaterialTitle(product.getTitle());
                purchaseOrderVo.setUnitId(product.getUnitId());
                purchaseOrderVo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
            }
        } else {
            MaterialVo material = this.proFromRedis.getMaterial(purchaseOrderVo.getMaterialId());
            if (ObjUtil.isNotNull(material)) {
                purchaseOrderVo.setMaterialCode(material.getCode());
                purchaseOrderVo.setMaterialTitle(material.getTitle());
                purchaseOrderVo.setUnitId(material.getUnitId());
                purchaseOrderVo.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
            }
        }

        return purchaseOrderVo;
    }

    @Override
    public SaResult insertPurchaseOrder(PurchaseOrderEntity purchaseOrder) {
        long l = this.checkCodeExist(purchaseOrder);
        if (l > 0L) {
            return SaResult.error("请购单对应的采购订单已存在");
        }

        SupplierGoodsVo supplierGoodsVo = this.planFromRedis.getSupplierMaterial(purchaseOrder.getMaterialId());
        if (ObjUtil.isNotNull(supplierGoodsVo)) {
            purchaseOrder.setSupplierId(supplierGoodsVo.getPid());
            purchaseOrder.setPrice(supplierGoodsVo.getPrice());
            purchaseOrder.setTaxRate(supplierGoodsVo.getTaxRate());
            purchaseOrder.setTaxPrice(supplierGoodsVo.getTaxPrice());
        } else {
            return SaResult.error("物料未绑定供应商");
        }

        Date date = new Date();
        Long count = this.lambdaQuery().between(PurchaseOrderEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        purchaseOrder.setOrderNo("CGD" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + count * 3));
        this.save(purchaseOrder);

        // 修改请购单状态
        this.requisitionService.lambdaUpdate().set(RequisitionEntity::getStatus, 1)
                .set(RequisitionEntity::getUpdateTime, LocalDateTime.now())
                .set(RequisitionEntity::getUpdateUser, StpUtil.getLoginIdAsLong())
                .eq(RequisitionEntity::getId, purchaseOrder.getRequisitionId())
                .update();

        return SaResult.data(purchaseOrder);
    }

    @Override
    public SaResult updatePurchaseOrder(PurchaseOrderEntity purchaseOrder) {
        this.updateById(purchaseOrder);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public PurchaseOrderEntity getBySiCode(String siCode) {
        return this.baseMapper.getBySiCode(siCode);
    }

    @Override
    public SaResult deletePurchaseOrder(List<Long> idList) {
        PurchaseOrderEntity purchaseOrder = this.getById(idList.get(0));
        // 请购单状态恢复
        this.requisitionService.lambdaUpdate().set(RequisitionEntity::getStatus, 0)
                .eq(RequisitionEntity::getId, purchaseOrder.getRequisitionId()).update();
        this.removeById(idList.get(0));
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private long checkCodeExist(PurchaseOrderEntity purchaseOrder) {
        LambdaQueryWrapper<PurchaseOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseOrderEntity::getRequisitionId, purchaseOrder.getRequisitionId());

        Long id = purchaseOrder.getId();
        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(PurchaseOrderEntity::getId, id);
        }

        wrapper.last(CheckToolUtil.LIMIT);
        return this.count(wrapper);
    }

}

