package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.CustomProductEntity;
import com.senmol.mes.plan.entity.OutboundEntity;
import com.senmol.mes.plan.entity.OutboundMxEntity;
import com.senmol.mes.plan.entity.PurchaseReturns;
import com.senmol.mes.plan.service.CustomProductService;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutboundService;
import com.senmol.mes.plan.service.PurchaseReturnsService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.RetrievalEntity;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.mapper.RetrievalMapper;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.RetrievalService;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.warehouse.vo.RetrievalInfo;
import com.senmol.mes.warehouse.vo.RetrievalMxVo;
import com.senmol.mes.warehouse.vo.StockVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private StorageReservaService storageReservaService;
    @Resource
    private PurchaseReturnsService purchaseReturnsService;
    @Resource
    private StockService stockService;
    @Resource
    private StorageService storageService;

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
    public int getTodayCount(String date) {
        return this.baseMapper.getTodayCount(date);
    }

    @Override
    public SaResult insertRetrievalInfo(RetrievalInfo info) {
        OutboundEntity outbound =
                this.outboundService.lambdaQuery().eq(OutboundEntity::getCode, info.getPickOrder()).one();
        if (ObjUtil.isNull(outbound) || outbound.getStatus() == 2) {
            return SaResult.error("出库单已完成出库");
        }

        List<Long> goodsIds = this.outboundMxService.getNotShip(outbound.getId());
        List<RetrievalMxEntity> mxs = info.getMxs();
        mxs.removeIf(item -> !goodsIds.contains(item.getGoodsId()));

        RetrievalEntity retrieval = new RetrievalEntity();
        String date = LocalDate.now().toString();
        int total = this.baseMapper.getTodayCount(date);
        retrieval.setBatchNo("CK" + date.replace("-", "") + (101 + total * 3));
        retrieval.setPickOrder(info.getPickOrder());
        retrieval.setPicker(info.getPicker());
        retrieval.setType(info.getType());
        boolean b = this.save(retrieval);
        if (b) {
            mxs.forEach(item -> item.setRetrievalId(retrieval.getId()));
            b = this.retrievalMxService.saveBatch(mxs);
            if (b) {
                try {
                    this.changeOutbound(mxs, retrieval.getPickOrder(), StpUtil.getLoginIdAsLong());
                } catch (Exception e) {
                    log.error("出库单变更失败", e);
                    throw new BusinessException("出库单变更失败");
                }

                try {
                    this.changeStock(mxs, retrieval);
                } catch (Exception e) {
                    log.error("仓库数量变更失败", e);
                    throw new BusinessException("仓库数量变更失败");
                }

                try {
                    this.changeStorage(mxs, retrieval);
                } catch (Exception e) {
                    log.error("库存剩余量变更失败", e);
                    throw new BusinessException("库存剩余量变更失败");
                }
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public boolean saveRetrieval(RetrievalEntity retrieval) {
        return this.baseMapper.saveRetrieval(retrieval) > 0;
    }

    /**
     * 修改出库单状态
     */
    private void changeOutbound(List<RetrievalMxEntity> mxs, String outBoundCode, Long userId) {
        OutboundEntity outbound =
                this.outboundService.lambdaQuery().eq(OutboundEntity::getCode, outBoundCode).last(CheckToolUtil.LIMIT).one();
        if (ObjUtil.isNotNull(outbound)) {
            Long id = outbound.getId();
            Map<Long, BigDecimal> map = mxs.stream().collect(Collectors.groupingBy(
                    RetrievalMxEntity::getGoodsId, Collectors.reducing(BigDecimal.ZERO, RetrievalMxEntity::getQty,
                            BigDecimal::add)));
            List<OutboundMxEntity> list = this.outboundMxService.lambdaQuery().eq(OutboundMxEntity::getOutboundId,
                    id).list();

            int status = 2;
            List<CountVo> mxVos = new ArrayList<>(map.size());
            List<CountVo> vos = this.retrievalMxService.getByObCode(outBoundCode);
            for (OutboundMxEntity mx : list) {
                Long goodsId = mx.getGoodsId();
                BigDecimal decimal = map.get(goodsId);
                if (decimal != null) {
                    CountVo countVo =
                            vos.stream().filter(item -> item.getAId().equals(goodsId)).findFirst().orElse(null);
                    if (ObjUtil.isNotNull(countVo)) {
                        decimal = decimal.add(countVo.getQty());
                    }

                    mx.setActQty(decimal);
                    mxVos.add(new CountVo(id, goodsId, null, decimal));
                }

                BigDecimal actQty = mx.getActQty();
                if (ObjUtil.isNull(actQty) || mx.getQty().compareTo(mx.getActQty()) > 0) {
                    status = 4;
                }
            }

            // 质量出库创建保留品数据
            if (status == 2 && outbound.getType() == 21) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.toLocalDate().toString();
                int count = this.storageReservaService.getTodayCount(date, 2);
                String format = date.replace("-", "");

                List<StorageReserva> reservas = new ArrayList<>(mxs.size());
                StorageReserva reserva;
                for (OutboundMxEntity mx : list) {
                    reserva = new StorageReserva();
                    reserva.setId(IdUtil.getSnowflakeNextId());
                    reserva.setCode("ZL" + format + (101 + count++ * 3));
                    reserva.setPid(outbound.getId());
                    reserva.setReceiptCode(outbound.getCode());
                    reserva.setDetectionWay(1);
                    reserva.setGoodsId(mx.getGoodsId());
                    reserva.setType(mx.getType());
                    reserva.setUnqualifiedQty(mx.getActQty());
                    reserva.setDisposal(2);
                    reserva.setSource(2);
                    reserva.setTester(userId);
                    reserva.setCreateTime(now);
                    reservas.add(reserva);
                }

                this.storageReservaService.saveReservaBatch(reservas);
            }

            this.outboundService.lambdaUpdate().set(OutboundEntity::getStatus, status).eq(OutboundEntity::getId, id).update();
            this.outboundMxService.modifyBatch(mxVos);
            this.purchaseReturnsService.lambdaUpdate().set(PurchaseReturns::getStatus, 1).eq(PurchaseReturns::getId,
                    outbound.getPid()).update();
        }
    }

    private void changeStock(List<RetrievalMxEntity> mxs, RetrievalEntity retrieval) {
        List<StockEntity> stocks = new ArrayList<>(mxs.size());

        for (RetrievalMxEntity mx : mxs) {
            StockEntity stock = new StockEntity();
            stock.setId(mx.getStockId());
            stock.setQty(mx.getQty());
            stocks.add(stock);
        }

        // 批量修改库位数据
        this.stockService.subModifyBatch(stocks, retrieval.getCreateTime(), retrieval.getCreateUser());
    }

    private void changeStorage(List<RetrievalMxEntity> mxs, RetrievalEntity retrieval) {
        List<StorageEntity> storages = new ArrayList<>(mxs.size());
        for (RetrievalMxEntity mx : mxs) {
            StorageEntity storage = new StorageEntity();
            storage.setId(mx.getStorageId());
            storage.setResidueQty(mx.getQty());
            storages.add(storage);
        }

        // 批量修改入库数据
        this.storageService.modifyBatch(storages, retrieval.getCreateTime(), retrieval.getCreateUser());
    }

}

