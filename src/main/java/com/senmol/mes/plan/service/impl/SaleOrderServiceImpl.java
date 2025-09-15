package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.entity.MrpEntity;
import com.senmol.mes.plan.entity.SaleOrderEntity;
import com.senmol.mes.plan.entity.SaleOrderProductEntity;
import com.senmol.mes.plan.mapper.SaleOrderMapper;
import com.senmol.mes.plan.page.DeliveryVoPage;
import com.senmol.mes.plan.page.OrderMxPage;
import com.senmol.mes.plan.service.MrpService;
import com.senmol.mes.plan.service.SaleOrderProductService;
import com.senmol.mes.plan.service.SaleOrderService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.plan.vo.*;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 销售订单(SaleOrder)表服务实现类
 *
 * @author makejava
 * @since 2023-03-13 13:29:46
 */
@Service("saleOrderService")
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrderEntity> implements SaleOrderService {

    @Resource
    private PlanFromRedis planFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SaleOrderProductService saleOrderProductService;
    @Resource
    private MrpService mrpService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public SaResult selectAll(Page<SaleOrderEntity> page, SaleOrderEntity saleOrder, Integer isMrp) {
        if (ObjectUtil.isNotNull(isMrp) && isMrp != 1) {
            this.page(page, new LambdaQueryWrapper<>(saleOrder).gt(SaleOrderEntity::getStatus, 0));
        } else {
            this.page(page, new QueryWrapper<>(saleOrder));
        }

        List<SaleOrderEntity> records = page.getRecords();
        records.forEach(item -> item.setCustomTitle(this.planFromRedis.getCustom(item.getCustomId())));
        return SaResult.data(page);
    }

    @Override
    public SaResult selectOne(Long id) {
        SaleOrderEntity saleOrder = this.getById(id);
        SaleOrderVo vo = Convert.convert(SaleOrderVo.class, saleOrder);
        vo.setCustomTitle(this.planFromRedis.getCustom(saleOrder.getCustomId()));

        List<SaleOrderProductEntity> products = this.saleOrderProductService.lambdaQuery()
                .eq(SaleOrderProductEntity::getSaleOrderId, id)
                .list();
        products.forEach(item -> {
            item.setSendQty(item.getRealityQty());

            ProductVo product = this.proFromRedis.getProduct(item.getProductId());
            if (ObjUtil.isNotNull(product)) {
                item.setProductCode(product.getCode());
                item.setProductTitle(product.getTitle());
                item.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
            }
        });

        vo.setProducts(products);
        return SaResult.data(vo);
    }

    @Override
    public SaResult orderMx(OrderMxPage page, OrderMxPojo pojo) {
        // 查询销售订单
        List<OrderMxVo> vos = this.baseMapper.orderMx(page, pojo);
        if (CollUtil.isEmpty(vos)) {
            return SaResult.data(vos);
        }

        CompletableFuture<List<OrderMxVo>> orderMx = CompletableFuture.supplyAsync(() -> this.setVos(vos),
                        this.executor).exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("销售订单明细列表查询失败，请重试");
                });

        CompletableFuture<OrderMxVoTotal> selectTotal =
                CompletableFuture.supplyAsync(() -> this.baseMapper.selectOrderMxTotal(page.getStartTime(),
                                page.getEndTime(), pojo), this.executor).exceptionally(e -> {
                            e.printStackTrace();
                            throw new BusinessException("合计统计失败，请重试");
                        });

        OrderMxPage join = orderMx.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public SaResult deliveryMx(DeliveryVoPage page, OrderMxPojo pojo) {
        CompletableFuture<List<DeliveryVo>> selectAll = CompletableFuture.supplyAsync(() -> this.selectAll(page, pojo), this.executor)
                .exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("销售发货明细列表查询失败，请重试");
                });

        if (page.getSize() == -1) {
            page.setRecords(selectAll.join());
            return SaResult.data(page);
        }

        CompletableFuture<DeliveryVoTotal> selectTotal =
                CompletableFuture.supplyAsync(() -> this.selectTotal(page, pojo), this.executor)
                        .exceptionally(e -> {
                            e.printStackTrace();
                            throw new BusinessException("合计统计失败，请重试");
                        });

        DeliveryVoPage join = selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public SaResult insertSaleOrder(SaleOrderEntity saleOrder) {
        int count = this.baseMapper.count();
        String code = "XSDD-" + DateUtil.format(new Date(), "yyyy-MMdd-") + ++count;
        saleOrder.setCode(code);

        this.save(saleOrder);

        List<SaleOrderProductEntity> products = saleOrder.getProducts();
        products.forEach(item -> item.setSaleOrderId(saleOrder.getId()));
        this.saleOrderProductService.saveBatch(products);

        return SaResult.data(saleOrder);
    }

    @Override
    public SaResult updateSaleOrder(SaleOrderEntity saleOrder) {
        List<SaleOrderProductEntity> products = saleOrder.getProducts();
        if (CollUtil.isNotEmpty(products) && saleOrder.getStatus() != 2) {
            this.saleOrderProductService.lambdaUpdate().eq(SaleOrderProductEntity::getSaleOrderId, saleOrder.getId()).remove();
            products.forEach(item -> item.setSaleOrderId(saleOrder.getId()));
            this.saleOrderProductService.saveBatch(products);
        }

        this.updateById(saleOrder);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SaResult closeOrder(Long id, Long productId) {
        this.saleOrderProductService.lambdaUpdate()
                .set(SaleOrderProductEntity::getStatus, 1)
                .eq(SaleOrderProductEntity::getSaleOrderId, id)
                .eq(SaleOrderProductEntity::getProductId, productId)
                .update();

        Long count = this.saleOrderProductService.lambdaQuery()
                .eq(SaleOrderProductEntity::getSaleOrderId, id)
                .eq(SaleOrderProductEntity::getStatus, 0)
                .count();
        if (count == 0L) {
            this.lambdaUpdate()
                    .set(SaleOrderEntity::getSign, 2)
                    .set(SaleOrderEntity::getStatus, 2)
                    .eq(SaleOrderEntity::getId, id)
                    .update();
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteSaleOrder(Long id) {
        this.mrpService.lambdaUpdate().eq(MrpEntity::getSaleOrderId, id).remove();
        this.saleOrderProductService.lambdaUpdate().eq(SaleOrderProductEntity::getSaleOrderId, id).remove();

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private List<OrderMxVo> setVos(List<OrderMxVo> vos) {
        boolean notHasOrder = false, notHasMx = false;
        // 获取销售订单编号
        Set<String> codes = vos.stream().map(OrderMxVo::getCode).collect(Collectors.toSet());
        // 查询任务单
        List<OrderVo> orders = this.baseMapper.workOrder(codes);
        List<OrderMxInspectVo> mxs = null;
        if (CollUtil.isNotEmpty(orders)) {
            // 获取任务单ID
            Set<Long> ids = orders.stream().map(OrderVo::getId).collect(Collectors.toSet());
            // 查询工单、质检
            mxs = this.baseMapper.orderMxInspect(ids);
            if (CollUtil.isEmpty(mxs)) {
                notHasMx = true;
            }
        } else {
            notHasOrder = true;
        }

        for (OrderMxVo vo : vos) {
            // 客户、经办人
            vo.setCustomTitle(this.planFromRedis.getCustom(vo.getCustomId()));
            vo.setCreateUserName(this.sysFromRedis.getUser(vo.getCreateUser()));

            // 产品信息
            ProductVo product = this.proFromRedis.getProduct(vo.getProductId());
            if (ObjUtil.isNotNull(product)) {
                vo.setProductCode(product.getCode());
                vo.setProductTitle(product.getTitle());
            }

            // 未发货数量
            vo.setUnshippedQty(vo.getQty().subtract(vo.getRealityQty()));

            if (notHasOrder) {
                continue;
            }
            // 任务单入库数量
            orders.stream()
                    .filter(item -> item.getCode().equals(vo.getCode()) && item.getProductId().equals(vo.getProductId()))
                    .findFirst()
                    .ifPresent(item -> {
                        vo.setOrderId(item.getId());
                        vo.setRecQty(item.getRecQty());
                    });

            if (notHasMx) {
                continue;
            }
            // 工单质检数据
            mxs.stream()
                    .filter(item -> item.getPid().equals(vo.getOrderId()))
                    .findFirst()
                    .ifPresent(item -> {
                        vo.setProduction(item.getProduction());
                        vo.setScrapQty(item.getDefective().add(item.getUnqualifiedQty()));
                    });
        }

        return vos;
    }

    private List<DeliveryVo> selectAll(DeliveryVoPage page, OrderMxPojo pojo) {
        List<DeliveryVo> list = this.baseMapper.deliveryMx(page, pojo);
        for (DeliveryVo vo : list) {
            ProductVo product = this.proFromRedis.getProduct(vo.getProductId());
            if (ObjUtil.isNotNull(product)) {
                vo.setProductCode(product.getCode());
                vo.setProductTitle(product.getTitle());
                vo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
            }

            vo.setCustomTitle(this.planFromRedis.getCustom(vo.getCustomId()));
            vo.setCreateUserName(this.sysFromRedis.getUser(vo.getCreateUser()));
        }

        return list;
    }

    private DeliveryVoTotal selectTotal(DeliveryVoPage page, OrderMxPojo pojo) {
        if (ObjUtil.isNotNull(pojo.getType())) {
            return this.baseMapper.selectDeliveryTotal(page.getStartTime(), page.getEndTime(), pojo, pojo.getType());
        } else {
            DeliveryVoTotal total = this.baseMapper.selectDeliveryTotal(page.getStartTime(), page.getEndTime(), pojo, 1);
            DeliveryVoTotal temp = this.baseMapper.selectDeliveryTotal(page.getStartTime(), page.getEndTime(), pojo, 2);
            total.setTotalQty(total.getTotalQty().subtract(temp.getTotalQty()));
            total.setTotalPrice(total.getTotalPrice().subtract(temp.getTotalPrice()));
            total.setTotalAmount(total.getTotalAmount().subtract(temp.getTotalAmount()));
            total.setTotalTaxPrice(total.getTotalTaxPrice().subtract(temp.getTotalTaxPrice()));
            total.setTotalPriceTax(total.getTotalPriceTax().subtract(temp.getTotalPriceTax()));
            return total;
        }
    }

}

