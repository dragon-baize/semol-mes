package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.entity.PurchaseInvoice;
import com.senmol.mes.plan.entity.PurchaseInvoiceMx;
import com.senmol.mes.plan.entity.PurchaseReturns;
import com.senmol.mes.plan.mapper.PurchaseInvoiceMapper;
import com.senmol.mes.plan.page.PurchaseInvoicePage;
import com.senmol.mes.plan.service.PurchaseInvoiceMxService;
import com.senmol.mes.plan.service.PurchaseInvoiceService;
import com.senmol.mes.plan.service.PurchaseReturnsService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.plan.vo.PurchaseInvoiceMxVo;
import com.senmol.mes.plan.vo.PurchaseInvoiceTotal;
import com.senmol.mes.plan.vo.PurchaseInvoiceVo;
import com.senmol.mes.plan.vo.SupplierVo;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 采购单开票(PurchaseInvoice)表服务实现类
 *
 * @author makejava
 * @since 2024-05-23 16:15:09
 */
@Service("purchaseInvoiceService")
public class PurchaseInvoiceServiceImpl extends ServiceImpl<PurchaseInvoiceMapper, PurchaseInvoice> implements PurchaseInvoiceService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private PlanFromRedis planFromRedis;
    @Resource
    private StorageService storageService;
    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private PurchaseReturnsService purchaseReturnsService;
    @Resource
    private PurchaseInvoiceMxService purchaseInvoiceMxService;

    @Override
    public SaResult selectMx(Long id) {
        List<PurchaseInvoiceMx> mxs =
                this.purchaseInvoiceMxService.lambdaQuery().eq(PurchaseInvoiceMx::getPid, id).list();
        if (CollUtil.isEmpty(mxs)) {
            return SaResult.ok();
        }

        List<PurchaseInvoiceMxVo> mxVos = Convert.toList(PurchaseInvoiceMxVo.class, mxs);

        List<Long> storageIds =
                mxVos.stream().filter(item -> item.getType() == 0).map(PurchaseInvoiceMxVo::getMxId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(storageIds)) {
            List<StorageEntity> list = this.storageService.lambdaQuery().in(StorageEntity::getId, storageIds).list();
            list.forEach(storage -> mxVos.stream().filter(item -> item.getMxId().equals(storage.getId()))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setBatchNo(storage.getBatchNo());
                        item.setGoodsId(storage.getGoodsId());
                        item.setGoodsType(storage.getType());
                        item.setCreateTime(storage.getCreateTime());
                        this.setValue(item);
                    }));
        }

        List<Long> returnsIds =
                mxVos.stream().filter(item -> item.getType() == 1).map(PurchaseInvoiceMxVo::getMxId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(returnsIds)) {
            List<PurchaseReturns> list = this.purchaseReturnsService.lambdaQuery().in(PurchaseReturns::getId,
                    returnsIds).list();
            list.forEach(returns -> mxVos.stream().filter(item -> item.getMxId().equals(returns.getId()))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setBatchNo(returns.getBatchNo());
                        item.setGoodsId(returns.getGoodsId());
                        item.setGoodsType(returns.getType());
                        item.setCreateTime(returns.getCreateTime());
                        this.setValue(item);
                    }));
        }

        return SaResult.data(mxVos);
    }

    @Override
    public SaResult selectAll(PurchaseInvoicePage page, PurchaseInvoiceVo purchaseInvoiceVo) {
        CompletableFuture<List<PurchaseInvoiceVo>> selectAll = CompletableFuture.supplyAsync(() -> {
            List<PurchaseInvoiceVo> list = this.baseMapper.selectAll(page, purchaseInvoiceVo);
            list.forEach(item -> {
                this.setSupplierName(item);
                this.setGoods(item);

                item.setUserName(this.sysFromRedis.getUser(item.getUserId()));
                item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
            });

            return list;
        }, this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("采购开票列表查询失败，请重试");
        });

        if (page.getSize() == -1) {
            page.setRecords(selectAll.join());
            return SaResult.data(page);
        }

        CompletableFuture<PurchaseInvoiceTotal> selectTotal =
                CompletableFuture.supplyAsync(() -> this.baseMapper.selectTotal(page.getStartTime(),
                        page.getEndTime(), purchaseInvoiceVo), this.executor).exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("合计统计失败，请重试");
                });

        PurchaseInvoicePage join = selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public SaResult insert(PurchaseInvoice purchaseInvoice) {
        List<PurchaseInvoiceMx> mxs = purchaseInvoice.getMxs();
        List<Long> mxIds = mxs.stream().map(PurchaseInvoiceMx::getMxId).collect(Collectors.toList());
        Long count = this.purchaseInvoiceMxService.lambdaQuery().in(PurchaseInvoiceMx::getMxId, mxIds).count();
        if (count > 0L) {
            return SaResult.error("存在已开票数据");
        }

        Date date = new Date();
        Long total = this.lambdaQuery().between(PurchaseInvoice::getCreateTime, DateUtil.beginOfDay(date),
                DateUtil.endOfDay(date)).count();
        String code = "CGKP" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + total * 3);
        purchaseInvoice.setCode(code);
        boolean b = this.save(purchaseInvoice);
        if (b) {
            Long id = purchaseInvoice.getId();
            mxs.forEach(item -> item.setPid(id));
            this.purchaseInvoiceMxService.saveBatch(mxs);

            List<Long> storageIds =
                    mxs.stream().filter(item -> item.getType() == 0).map(PurchaseInvoiceMx::getMxId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(storageIds)) {
                this.storageService.setInvoices(storageIds, id);
            }

            List<Long> returnsIds =
                    mxs.stream().filter(item -> item.getType() == 1).map(PurchaseInvoiceMx::getMxId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(returnsIds)) {
                this.purchaseReturnsService.setInvoices(returnsIds, id);
            }
        }

        return SaResult.data(code);
    }

    @Override
    public SaResult modifyMx(List<PurchaseInvoiceMx> list) {
        if (CollUtil.isNotEmpty(list)) {
            Long pid = list.get(0).getPid();
            this.purchaseInvoiceMxService.updateBatch(pid, list);

            List<PurchaseInvoiceMx> mxList = this.purchaseInvoiceMxService.lambdaQuery().eq(PurchaseInvoiceMx::getPid
                    , pid).list();

            BigDecimal total = mxList.stream().map(item -> {
                BigDecimal amount = item.getQty().multiply(item.getTaxPrice());
                return item.getType() == 0 ? amount : amount.negate();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            this.lambdaUpdate().set(PurchaseInvoice::getTotal, total).eq(PurchaseInvoice::getId, pid).update();
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult delById(Long id) {
        PurchaseInvoice purchaseInvoice = this.getById(id);
        if (ObjUtil.isNull(purchaseInvoice)) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        this.purchaseInvoiceMxService.lambdaUpdate().eq(PurchaseInvoiceMx::getPid, id).remove();
        this.storageService.lambdaUpdate().set(StorageEntity::getInvoice, null).eq(StorageEntity::getInvoice, id).update();
        this.purchaseReturnsService.lambdaUpdate().set(PurchaseReturns::getInvoice, null).eq(PurchaseReturns::getInvoice, id).update();
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private void setValue(PurchaseInvoiceMxVo vo) {
        if (vo.getGoodsType() > 1) {
            MaterialVo material = this.proFromRedis.getMaterial(vo.getGoodsId());
            vo.setGoodsCode(material.getCode());
            vo.setGoodsTitle(material.getTitle());
            vo.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
        } else {
            ProductVo product = this.proFromRedis.getProduct(vo.getGoodsId());
            vo.setGoodsCode(product.getCode());
            vo.setGoodsTitle(product.getTitle());
            vo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
        }
    }

    private void setSupplierName(PurchaseInvoiceVo item) {
        Arrays.stream(item.getSupplierIds().split(",")).forEach(supplierId -> {
            SupplierVo supplierVo = this.planFromRedis.getSupplier(Long.valueOf(supplierId));
            if (ObjUtil.isNotNull(supplierVo)) {
                String supplierName = item.getSupplierName();
                if (StrUtil.isBlank(supplierName)) {
                    item.setSupplierName(supplierVo.getName());
                } else {
                    item.setSupplierName(supplierName.concat(",").concat(supplierVo.getName()));
                }
            }
        });
    }

    private void setGoods(PurchaseInvoiceVo item) {
        Arrays.stream(item.getGoodsIds().split(",")).forEach(goodsType -> {
            String[] split = goodsType.split(":");
            if (Integer.parseInt(split[0]) == 2) {
                MaterialVo material = this.proFromRedis.getMaterial(Long.valueOf(split[1]));
                if (ObjUtil.isNotNull(material)) {
                    String goodsCode = item.getGoodsCode();
                    if (StrUtil.isBlank(goodsCode)) {
                        item.setGoodsCode(material.getCode());
                    } else {
                        item.setGoodsCode(goodsCode.concat(",").concat(material.getCode()));
                    }

                    String goodsTitle = item.getGoodsTitle();
                    if (StrUtil.isBlank(goodsTitle)) {
                        item.setGoodsTitle(material.getTitle());
                    } else {
                        item.setGoodsTitle(goodsTitle.concat(",").concat(material.getTitle()));
                    }
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(Long.valueOf(split[1]));
                if (ObjUtil.isNotNull(product)) {
                    String goodsCode = item.getGoodsCode();
                    if (StrUtil.isBlank(goodsCode)) {
                        item.setGoodsCode(product.getCode());
                    } else {
                        item.setGoodsCode(goodsCode.concat(",").concat(product.getCode()));
                    }

                    String goodsTitle = item.getGoodsTitle();
                    if (StrUtil.isBlank(goodsTitle)) {
                        item.setGoodsTitle(product.getTitle());
                    } else {
                        item.setGoodsTitle(goodsTitle.concat(",").concat(product.getTitle()));
                    }
                }
            }
        });
    }

}

