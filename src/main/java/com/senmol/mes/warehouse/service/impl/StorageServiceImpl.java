package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.entity.MaterialEntity;
import com.senmol.mes.produce.entity.ProductEntity;
import com.senmol.mes.produce.service.MaterialService;
import com.senmol.mes.produce.service.ProductService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.mapper.StorageMapper;
import com.senmol.mes.warehouse.page.*;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.warehouse.vo.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 入库记录(Storage)表服务实现类
 *
 * @author makejava
 * @since 2023-07-24 15:58:02
 */
@Service("storageService")
public class StorageServiceImpl extends ServiceImpl<StorageMapper, StorageEntity> implements StorageService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StockService stockService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private RedisService redisService;
    @Resource
    private WarAsyncUtil warAsyncUtil;
    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private MaterialService materialService;
    @Resource
    private ProductService productService;

    @Override
    public BigDecimal getStorage(Long productId) {
        List<StorageEntity> storages = this.lambdaQuery()
                .eq(StorageEntity::getGoodsId, productId)
                .gt(StorageEntity::getStatus, 0)
                .gt(StorageEntity::getResidueQty, 0)
                .list();

        BigDecimal bigDecimal = new BigDecimal(0);
        for (StorageEntity storage : storages) {
            bigDecimal = bigDecimal.add(storage.getResidueQty());
        }

        return bigDecimal;
    }

    @Override
    public Page<StorageVo> selectAll(StoragePage page, StorageEntity storage) {
        CompletableFuture<List<StorageVo>> selectAll = CompletableFuture.supplyAsync(() -> {
            List<StorageVo> vos = this.baseMapper.selectAll(page, storage);

            for (StorageVo vo : vos) {
                StockVo stockVo = this.warAsyncUtil.getStock(vo.getStockId());
                if (ObjUtil.isNotNull(stockVo)) {
                    vo.setStockCode(stockVo.getCode());
                    vo.setStockTitle(stockVo.getTitle());
                }

                // 字典
                vo.setUnitTitle(this.sysFromRedis.getDictMx(vo.getUnitId()));
            }

            return vos;
        }, this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("入库数据列表查询失败，请重试");
        });

        if (page.getSize() == -1) {
            page.setRecords(selectAll.join());
            return page;
        }

        CompletableFuture<StorageTotal> selectTotal = CompletableFuture.supplyAsync(() ->
                        this.baseMapper.selectTotal(page.getStartTime(), page.getEndTime(), storage), this.executor)
                .exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("合计统计失败，请重试");
                });

        return selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();
    }

    @Override
    public List<CountVo> getKcl(Set<Long> goodIds, Integer type) {
        return this.baseMapper.getKcl(goodIds, type);
    }

    @Override
    public Page<StorageVo> getList(Page<StorageVo> page, StorageEntity storage) {
        List<StorageVo> vos = this.baseMapper.getList(page, storage);

        for (StorageVo vo : vos) {
            StockVo stockVo = this.warAsyncUtil.getStock(vo.getStockId());
            if (ObjUtil.isNotNull(stockVo)) {
                vo.setStockCode(stockVo.getCode());
                vo.setStockTitle(stockVo.getTitle());
            }

            // 字典
            vo.setUnitTitle(this.sysFromRedis.getDictMx(vo.getUnitId()));
        }

        page.setRecords(vos);
        return page;
    }

    @Override
    public List<StorageVo> getByStockId(Long stockId, Integer type) {
        return this.baseMapper.getByStockId(stockId, type);
    }

    @Override
    public Page<StorageInfo> overviewData(Page<StorageInfo> page, String queryParam, Integer type) {
        List<StorageInfo> infos = this.baseMapper.overviewData(page, queryParam, type);

        for (StorageInfo info : infos) {
            info.setUnitTitle(this.sysFromRedis.getDictMx(info.getUnitId()));
        }

        page.setRecords(infos);
        return page;
    }

    @Override
    public SaResult summary(SummaryPage page, Summary summary) {
        CompletableFuture<List<Summary>> selectAll = CompletableFuture.supplyAsync(() -> {
            Integer type = summary.getType();
            if (type > 1) {
                Page<MaterialEntity> entityPage = new Page<>(page.getCurrent(), page.getSize());
                MaterialEntity material = new MaterialEntity();
                material.setType(Math.abs(type - 3));
                this.materialService.page(entityPage, new QueryWrapper<>(material));
                page.setTotal(entityPage.getTotal());
                return this.calculate(entityPage.getRecords(), summary, type);
            } else {
                Page<ProductEntity> entityPage = new Page<>(page.getCurrent(), page.getSize());
                ProductEntity product = new ProductEntity();
                product.setType(Math.abs(type - 1));
                this.productService.page(entityPage, new QueryWrapper<>(product));
                page.setTotal(entityPage.getTotal());
                return this.calculate(entityPage.getRecords(), summary, type);
            }
        }, this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("库存汇总列表查询失败，请重试");
        });

        CompletableFuture<SummaryTotal> selectTotal = CompletableFuture.supplyAsync(() ->
                this.getTotal(summary), this.executor).exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("库存汇总合计计算失败，请重试");
        });

        SummaryPage join = selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public InventoryPage inventory(InventoryPage page, Inventory inventory) {
        CompletableFuture<List<Inventory>> selectAll =
                CompletableFuture.supplyAsync(() -> this.baseMapper.inventory(page, inventory), this.executor)
                .exceptionally(e -> {
                    e.printStackTrace();
                    throw new BusinessException("出入库明细列表查询失败，请重试");
                });

        CompletableFuture<BigDecimal> selectTotal = CompletableFuture.supplyAsync(() ->
                        this.baseMapper.sumInventory(inventory, page.getStartTime(), page.getEndTime()), this.executor)
                .exceptionally(e -> {
            e.printStackTrace();
            throw new BusinessException("合计统计失败，请重试");
        });

        return selectAll.thenCombine(selectTotal, (records, amount) -> {
            page.setRecords(records);
            page.setAmount(amount);
            return page;
        }).join();
    }

    @Override
    public List<GoodsMx> goodsMx(Long goodsId) {
        return this.baseMapper.goodsMx(goodsId);
    }

    @Override
    public SaResult insertStorage(StorageVo storageVo) {
        Long count = this.lambdaQuery().eq(StorageEntity::getBatchNo, storageVo.getBatchNo()).count();
        if (count > 0L) {
            return SaResult.error("批次号重复");
        }

        // 保存入库数据
        StorageEntity storage = Convert.convert(StorageEntity.class, storageVo);
        storage.setResidueQty(storage.getQty());
        boolean b = this.save(storage);
        if (b) {
            CompletableFuture.allOf(
                    this.warAsyncUtil.updateStock(storage),
                    this.warAsyncUtil.updateStorageInspect(storage),
                    this.warAsyncUtil.updateReceipt(storage),
                    this.warAsyncUtil.updateWorkOrder(storage),
                    this.warAsyncUtil.updatePurchaseOrder(storage),
                    this.warAsyncUtil.updateOutsource(storage)
            ).exceptionally(e -> {
                e.printStackTrace();
                throw new BusinessException("入库失败，请重试");
            }).join();
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public void modifyBatch(List<StorageEntity> storages, LocalDateTime updateTime, Long userId) {
        this.baseMapper.modifyBatch(storages, updateTime, userId);
    }

    @Override
    public void updateBatchByBatchNo(List<StorageEntity> storages) {
        this.baseMapper.updateBatchByBatchNo(storages);
    }

    @Override
    public List<GoodsInfo> getGoodsByOutBoundCode(String code) {
        List<GoodsInfo> list = this.baseMapper.getGoodsByOutBoundCode(code);

        list.forEach(item -> {
            // 物料
            if (item.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(item.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    item.setGoodsCode(material.getCode());
                    item.setGoodsTitle(material.getTitle());
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(item.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    item.setGoodsCode(product.getCode());
                    item.setGoodsTitle(product.getTitle());
                }
            }

            StockVo stockVo = this.warAsyncUtil.getStock(item.getStockId());
            if (ObjUtil.isNotNull(stockVo)) {
                item.setStockCode(stockVo.getCode());
                item.setStockTitle(stockVo.getTitle());
            }
        });

        return list.stream().sorted(Comparator.comparing(GoodsInfo::getCreateTime)).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getQtyByPurchaseCode(String code) {
        return this.baseMapper.getQtyByPurchaseCode(code);
    }

    @Override
    public SaResult getByCgDh(String code, Long invoice) {
        List<StorageEntity> list = this.baseMapper.getByCgDh(Arrays.asList(code.split(",")), invoice);
        list.forEach(item -> {
            if (item.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(item.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    item.setGoodsCode(material.getCode());
                    item.setGoodsTitle(material.getTitle());
                    item.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(item.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    item.setGoodsCode(product.getCode());
                    item.setGoodsTitle(product.getTitle());
                    item.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            }
        });

        return SaResult.data(list);
    }

    @Override
    public List<CountVo> getByGoodsIds(Set<Long> goodsIds) {
        return this.baseMapper.getByGoodsIds(goodsIds);
    }

    @Override
    public BigDecimal getReturns(String receiptCode) {
        return this.baseMapper.getReturns(receiptCode);
    }

    @Override
    public List<Object> insertByExcel(List<Object> cachedDataList) {
        // 错误信息展示
        List<Object> failList = new ArrayList<>();

        // 批次号不能重复
        List<String> batchNos = this.lambdaQuery()
                .list()
                .stream()
                .map(StorageEntity::getBatchNo)
                .collect(Collectors.toList());

        // 获取所有物料/产品信息
        List<ProduceGoods> goods = this.getGoods();
        // 获取所有库位信息
        List<StockEntity> stocks = this.stockService.list();

        List<StorageEntity> storages = new ArrayList<>(cachedDataList.size());
        for (Object object : cachedDataList) {
            StorageVo vo = Convert.convert(StorageVo.class, object);

            if (batchNos.contains(vo.getBatchNo())) {
                vo.setRemarks("批次号重复【" + vo.getBatchNo() + "】");
                failList.add(vo);
                continue;
            }

            ProduceGoods storageVo = goods.stream()
                    .filter(item -> item.getGoodsCode().equals(vo.getGoodsCode()) && item.getType().equals(vo.getType()))
                    .findFirst()
                    .orElse(null);
            if (ObjectUtil.isNull(storageVo)) {
                vo.setRemarks("未匹配到对应的物料/产品信息");
                failList.add(vo);
                continue;
            }

            vo.setGoodsId(storageVo.getGoodsId());
            vo.setLifeInfo(storageVo.getLifeInfo());
            vo.setLifeType(storageVo.getLifeType());

            // 设置库位
            StockEntity stock = stocks.stream()
                    .filter(item -> item.getCode().equals(vo.getStockCode()))
                    .findFirst()
                    .orElse(null);
            if (BeanUtil.isEmpty(stock)) {
                vo.setRemarks("未匹配到对应的库位");
                failList.add(vo);
                continue;
            }

            vo.setStockId(stock.getId());
            stock.setQty(stock.getQty().add(vo.getQty()));

            vo.setResidueQty(vo.getQty());
            vo.setSource(2);
            storages.add(Convert.convert(StorageEntity.class, vo));
        }

        this.stockService.updateBatchById(stocks);
        this.saveBatch(storages);
        return failList;
    }

    @Override
    public void insertBatch(List<StorageEntity> entities) {
        this.baseMapper.insertBatch(entities);
    }

    @Override
    public void updateStatusByIds(List<StorageEntity> storages, LocalDateTime updateTime, Long updateUser) {
        this.baseMapper.updateStatusByIds(storages, updateTime, updateUser);
    }

    @Override
    public void setInvoices(List<Long> storageIds, Long invoice) {
        this.baseMapper.setInvoices(storageIds, invoice);
    }

    private List<ProduceGoods> getGoods() {
        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_GOODS.getKey());
        List<ProduceGoods> vos = Convert.toList(ProduceGoods.class, object);
        if (CollUtil.isEmpty(vos)) {
            vos = this.baseMapper.getGoods();
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_GOODS.getKey(), vos, RedisKeyEnum.PRODUCE_GOODS.getTimeout());
        return vos;
    }

    private List<Summary> calculate(List<?> list, Summary summary, Integer type) {
        List<Long> ids = list.stream().map(item -> {
            if (type < 2) {
                ProductEntity product = (ProductEntity) item;
                return product.getId();
            } else {
                MaterialEntity material = (MaterialEntity) item;
                return material.getId();
            }
        }).collect(Collectors.toList());

        LocalDate startTime = summary.getStartTime();
        LocalDateTime beginTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        if (ObjUtil.isNotNull(startTime)) {
            beginTime = startTime.atStartOfDay();
        }

        List<StorageEntity> storages = this.baseMapper.summary(ids, type, summary.getEndTime());
        Summary s;
        List<Summary> result = new ArrayList<>(ids.size());
        for (Object obj : list) {
            s = new Summary();
            Long goodsId;
            if (type < 2) {
                ProductEntity product = (ProductEntity) obj;
                goodsId = product.getId();
                s.setGoodsId(goodsId);
                s.setGoodsCode(product.getCode());
                s.setGoodsTitle(product.getTitle());
                s.setType(product.getType());
            } else {
                MaterialEntity material = (MaterialEntity) obj;
                goodsId = material.getId();
                s.setGoodsId(goodsId);
                s.setGoodsCode(material.getCode());
                s.setGoodsTitle(material.getTitle());
                s.setType(material.getType());
            }

            List<StorageEntity> tmp =
                    storages.stream().filter(item -> item.getGoodsId().equals(goodsId)).collect(Collectors.toList());
            for (StorageEntity storage : tmp) {
                BigDecimal qty = storage.getQty();
                BigDecimal residueQty = storage.getResidueQty();
                BigDecimal taxPrice = storage.getTaxPrice();

                if (storage.getCreateTime().isBefore(beginTime)) {
                    s.setPreQty(s.getPreQty().add(residueQty).stripTrailingZeros());
                    s.setPreAmount(s.getPreAmount().add(residueQty.multiply(taxPrice).stripTrailingZeros()));
                } else {
                    BigDecimal subtract = qty.subtract(residueQty);
                    s.setInQty(s.getInQty().add(qty).stripTrailingZeros());
                    s.setInAmount(s.getInAmount().add(qty.multiply(taxPrice).stripTrailingZeros()));
                    s.setOutQty(s.getOutQty().add(subtract).stripTrailingZeros());
                    s.setOutAmount(s.getOutAmount().add(subtract.multiply(taxPrice).stripTrailingZeros()));
                }

                if (type == 2) {
                    ProductVo product = this.proFromRedis.getProduct(storage.getId());
                    if (ObjUtil.isNotNull(product)) {
                        s.setProductCode(product.getCode());
                    }
                }
            }

            s.setLastQty(s.getPreQty().add(s.getInQty()).subtract(s.getOutQty()).stripTrailingZeros());
            s.setLastAmount(s.getPreAmount().add(s.getInAmount()).subtract(s.getOutAmount()).stripTrailingZeros());
            result.add(s);
        }

        return result;
    }

    private SummaryTotal getTotal(Summary summary) {
        LocalDate startTime = summary.getStartTime();
        SummaryTotal total = this.baseMapper.getTotal(startTime, summary.getEndTime(), summary);
        if (ObjUtil.isNull(total)) {
            return null;
        }

        if (ObjUtil.isNull(startTime)) {
            total.setTotalLastQty(total.getTotalInQty().subtract(total.getTotalOutQty()).stripTrailingZeros());
            total.setTotalLastAmount(total.getTotalInAmount().subtract(total.getTotalOutAmount()).stripTrailingZeros());
        } else {
            SummaryTotal preTotal = this.baseMapper.getPreTotal(summary.getType(), startTime);
            BigDecimal totalPreQty = preTotal.getTotalPreQty().stripTrailingZeros();
            BigDecimal totalPreAmount = preTotal.getTotalPreAmount().stripTrailingZeros();
            total.setTotalPreQty(totalPreQty);
            total.setTotalPreAmount(totalPreAmount);
            total.setTotalLastQty(totalPreQty.add(total.getTotalInQty()).subtract(total.getTotalOutQty()).stripTrailingZeros());
            total.setTotalLastAmount(totalPreAmount.add(total.getTotalInAmount()).subtract(total.getTotalOutAmount()).stripTrailingZeros());
        }

        total.setTotalInQty(total.getTotalInQty().stripTrailingZeros());
        total.setTotalInAmount(total.getTotalInAmount().stripTrailingZeros());
        total.setTotalOutQty(total.getTotalOutQty().stripTrailingZeros());
        total.setTotalOutAmount(total.getTotalOutAmount().stripTrailingZeros());
        return total;
    }

}

