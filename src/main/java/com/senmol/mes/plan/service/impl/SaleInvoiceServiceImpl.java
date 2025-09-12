package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.entity.SaleInvoice;
import com.senmol.mes.plan.entity.SaleInvoiceMx;
import com.senmol.mes.plan.mapper.SaleInvoiceMapper;
import com.senmol.mes.plan.page.SaleInvoicePage;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.service.SaleInvoiceMxService;
import com.senmol.mes.plan.service.SaleInvoiceService;
import com.senmol.mes.plan.vo.SaleInvoicePojo;
import com.senmol.mes.plan.vo.SaleInvoiceTotal;
import com.senmol.mes.plan.vo.SaleInvoiceVo;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 销售发货单开票(SaleInvoice)表服务实现类
 *
 * @author makejava
 * @since 2024-05-23 14:28:01
 */
@Service("saleInvoiceService")
public class SaleInvoiceServiceImpl extends ServiceImpl<SaleInvoiceMapper, SaleInvoice> implements SaleInvoiceService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SaleInvoiceMxService saleOrderMxService;
    @Resource
    private OutboundService outboundService;
    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private StorageReservaService storageReservaService;

    @Override
    public SaResult selectOne(Long id) {
        List<SaleInvoicePojo> list = this.baseMapper.getOne(id);
        list.forEach(item -> {
            ProductVo product = this.proFromRedis.getProduct(item.getGoodsId());
            if (ObjUtil.isNotNull(product)) {
                item.setGoodsCode(product.getCode());
                item.setGoodsTitle(product.getTitle());
                item.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
            }
        });

        return SaResult.data(list);
    }

    @Override
    public SaResult selectAll(SaleInvoicePage page, SaleInvoiceVo saleInvoiceVo) {
        CompletableFuture<List<SaleInvoiceVo>> selectAll = CompletableFuture.supplyAsync(() -> {
                    List<SaleInvoiceVo> list = this.baseMapper.selectAll(page, saleInvoiceVo);
                    list.forEach(item -> {
                        item.setAgentName(this.sysFromRedis.getUser(item.getAgent()));
                        item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));

                        if (StrUtil.isNotBlank(item.getGoodsIds())) {
                            String[] goodsIds = item.getGoodsIds().split(",");
                            Arrays.stream(goodsIds).forEach(goodsId -> {
                                ProductVo product = this.proFromRedis.getProduct(Long.valueOf(goodsId));
                                if (StrUtil.isBlank(item.getGoodsCode())) {
                                    item.setGoodsCode(product.getCode());
                                    item.setGoodsTitle(product.getTitle());
                                } else {
                                    item.setGoodsCode(item.getCode().concat(",").concat(product.getCode()));
                                    item.setGoodsTitle(item.getGoodsTitle().concat(",").concat(product.getTitle()));
                                }
                            });
                        }
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

        CompletableFuture<SaleInvoiceTotal> selectTotal =
                CompletableFuture.supplyAsync(() -> this.baseMapper.selectTotal(page.getStartTime(),
                                page.getEndTime(), saleInvoiceVo), this.executor)
                        .exceptionally(e -> {
                            e.printStackTrace();
                            throw new BusinessException("合计统计失败，请重试");
                        });

        SaleInvoicePage join = selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);

    }

    @Override
    public SaResult insert(SaleInvoice saleInvoice) {
        List<SaleInvoiceMx> mxs = saleInvoice.getMxs();
        Long count = this.baseMapper.check(mxs);
        if (count > 0L) {
            return SaResult.error("存在已开票数据");
        }

        boolean save = this.save(saleInvoice);
        if (save) {
            Long id = saleInvoice.getId();
            mxs.forEach(item -> item.setInvoiceId(id));
            boolean b = this.saleOrderMxService.saveBatch(mxs);
            if (b) {
                List<SaleInvoiceMx> send = mxs.stream().filter(item -> item.getType() == 1).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(send)) {
                    Set<Long> ids = send.stream().map(SaleInvoiceMx::getOutboundId).collect(Collectors.toSet());
                    this.outboundService.lambdaUpdate()
                            .set(OutboundEntity::getInvoice, id)
                            .in(OutboundEntity::getId, ids)
                            .update();
                }

                mxs.removeAll(send);
                if (CollUtil.isNotEmpty(mxs)) {
                    Set<Long> ids = mxs.stream().map(SaleInvoiceMx::getPid).collect(Collectors.toSet());
                    this.storageReservaService.lambdaUpdate()
                            .set(StorageReserva::getInvoice, id)
                            .in(StorageReserva::getId, ids)
                            .update();
                }
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

}

