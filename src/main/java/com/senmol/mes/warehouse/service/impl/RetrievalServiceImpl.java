package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.entity.CustomProductEntity;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.service.CustomProductService;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.mapper.RetrievalMapper;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.RetrievalService;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.warehouse.vo.RetrievalInfo;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;
import com.senmol.mes.warehouse.vo.StockVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 出库记录(Retrieval)表服务实现类
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Service("retrievalService")
public class RetrievalServiceImpl extends ServiceImpl<RetrievalMapper, RetrievalEntity> implements RetrievalService {

    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private WarAsyncUtil warAsyncUtil;
    @Resource
    private PlanFromRedis planFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private CustomProductService customProductService;
    @Resource
    private OutboundService outboundService;
    @Resource
    private OutboundMxService outboundMxService;

    @Override
    public RetrievalEntity selectOne(Long id, Integer type) {
        RetrievalEntity retrieval = this.getById(id);
        retrieval.setPickerName(this.sysFromRedis.getUser(retrieval.getPicker()));
        String pickOrder = retrieval.getPickOrder();
        List<RetrievalMxVo> mxVos = this.retrievalMxService.getByRetrievalId(pickOrder, id, type);

        List<CustomProductEntity> list = new ArrayList<>();
        if (type == 0) {
            list = this.customProductService.getByOutBoundCode(pickOrder);
            if (CollUtil.isNotEmpty(list)) {
                retrieval.setCustom(this.planFromRedis.getCustom(list.get(0).getCustomId()));
            }
        }

        for (RetrievalMxVo mxVo : mxVos) {
            if (type < 2) {
                ProductVo product = this.proFromRedis.getProduct(mxVo.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    mxVo.setGoodsCode(product.getCode());
                    mxVo.setGoodsTitle(product.getTitle());
                    mxVo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            } else {
                MaterialVo material = this.proFromRedis.getMaterial(mxVo.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    mxVo.setGoodsCode(material.getCode());
                    mxVo.setGoodsTitle(material.getTitle());
                    mxVo.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            }

            StockVo stockVo = this.warAsyncUtil.getStock(mxVo.getStockId());
            if (ObjUtil.isNotNull(stockVo)) {
                mxVo.setStockCode(stockVo.getCode());
                mxVo.setStockTitle(stockVo.getTitle());
            }

            if (type == 0) {
                list.stream()
                        .filter(item -> item.getProductId().equals(mxVo.getGoodsId()))
                        .findFirst()
                        .ifPresent(item -> mxVo.setCusProCode(item.getCusProCode()));
            }
        }

        retrieval.setMxVos(mxVos);
        return retrieval;
    }

    @Override
    public Page<RetrievalEntity> selectAll(Page<RetrievalEntity> page, RetrievalEntity retrieval) {
        this.page(page, new QueryWrapper<>(retrieval));
        List<RetrievalEntity> records = page.getRecords();
        records.forEach(item -> item.setPickerName(this.sysFromRedis.getUser(item.getPicker())));
        return page;
    }

    @Override
    public SaResult insertRetrievalInfo(RetrievalInfo info) {
        OutboundEntity outbound = this.outboundService.lambdaQuery().eq(OutboundEntity::getCode, info.getPickOrder()).one();
        if (ObjUtil.isNull(outbound) || outbound.getStatus() == 2) {
            return SaResult.error("出库单已完成出库");
        }

        List<Long> goodsIds = this.outboundMxService.getNotShip(outbound.getId());
        List<RetrievalMxEntity> mxs = info.getMxs();
        mxs.removeIf(item -> !goodsIds.contains(item.getGoodsId()));

        RetrievalEntity retrieval = new RetrievalEntity();
        Date date = new Date();
        Long total = this.lambdaQuery().between(RetrievalEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        retrieval.setBatchNo("CK" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + total * 3));
        retrieval.setPickOrder(info.getPickOrder());
        retrieval.setPicker(info.getPicker());
        retrieval.setType(info.getType());
        boolean b = this.save(retrieval);
        if (b) {
            mxs.forEach(item -> item.setRetrievalId(retrieval.getId()));
            b = this.retrievalMxService.saveBatch(mxs);
            if (b) {
                CompletableFuture<Void> changeOutbound = this.warAsyncUtil.changeOutbound(mxs,
                        retrieval.getPickOrder(), StpUtil.getLoginIdAsLong()).exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("出库单变更失败");
                });

                CompletableFuture<Void> changeStock = this.warAsyncUtil.changeStock(mxs, retrieval).exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("仓库数量变更失败");
                });

                CompletableFuture<Void> changeStorage = this.warAsyncUtil.changeStorage(mxs, retrieval).exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("库存剩余量变更失败");
                });

                CompletableFuture.allOf(changeOutbound, changeStock, changeStorage).join();
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public boolean saveRetrieval(RetrievalEntity retrieval) {
        return this.baseMapper.saveRetrieval(retrieval) > 0;
    }

}

