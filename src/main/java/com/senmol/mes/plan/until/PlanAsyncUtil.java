package com.senmol.mes.plan.until;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.entity.Supplier;
import com.senmol.mes.plan.entity.SupplierGoods;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutsourceService;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.plan.vo.SupplierGoodsVo;
import com.senmol.mes.plan.vo.SupplierVo;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class PlanAsyncUtil {

    @Resource
    private RedisService redisService;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private StorageService storageService;
    @Resource
    private OutsourceService outsourceService;
    @Resource
    private ProduceService produceService;

    /**
     * 供应商物料加入缓存
     */
    @Async
    public void delSupplierGoods(Supplier supplier, List<SupplierGoods> goods) {
        SupplierVo supplierVo = Convert.convert(SupplierVo.class, supplier);
        this.redisService.set(RedisKeyEnum.PLAN_SUPPLIER.getKey() + supplierVo.getId(), supplierVo,
                RedisKeyEnum.PLAN_SUPPLIER.getTimeout());

        for (SupplierGoods good : goods) {
            SupplierGoodsVo supplierGoodsVo = Convert.convert(SupplierGoodsVo.class, good);
            this.redisService.set(RedisKeyEnum.PLAN_MATERIAL.getKey() + good.getGoodsId(), supplierGoodsVo,
                    RedisKeyEnum.PLAN_MATERIAL.getTimeout());
        }
    }

    @Async
    public Future<Map<Long, BigDecimal>> wwJhInfo(Set<Long> materialIds) {
        List<OutsourceEntity> outsources = this.outsourceService.lambdaQuery()
                .lt(OutsourceEntity::getStatus, 3)
                .list();

        if (CollUtil.isEmpty(outsources)) {
            return new AsyncResult<>(new HashMap<>(0));
        }

        Map<Long, BigDecimal> map = MapUtil.newHashMap();
        Set<String> codes = new HashSet<>(outsources.size());

        for (OutsourceEntity outsource : outsources) {
            Long productId = outsource.getProductId();

            BomVo bom = this.proFromRedis.getBom(productId);
            ProductVo product = this.proFromRedis.getProduct(productId);
            if (ObjUtil.isNull(bom) || ObjUtil.isNull(product)) {
                continue;
            }

            List<BomMaterialVo> materialVos = bom.getMaterialVos()
                    .stream()
                    .filter(item -> materialIds.contains(item.getMaterialId()))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(materialVos)) {
                continue;
            }

            BigDecimal yield = product.getYield().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal cpQty = outsource.getQty();

            materialVos.forEach(item -> {
                Long materialId = item.getMaterialId();
                BigDecimal wlQty = map.get(materialId);
                BigDecimal divide = item.getQty().multiply(cpQty).divide(yield, 0, RoundingMode.UP);

                if (ObjUtil.isNotNull(wlQty)) {
                    divide = divide.add(wlQty);
                }

                map.put(materialId, divide);
            });

            codes.add(outsource.getCode());
        }

        if (CollUtil.isEmpty(codes)) {
            return new AsyncResult<>(new HashMap<>(0));
        }

        // 查询委外出库数量
        List<CountVo> ckQty = this.outboundMxService.getCkQty(codes, null, 1, null);
        if (CollUtil.isEmpty(ckQty)) {
            return new AsyncResult<>(map);
        }

        // 去除出库单数量
        Iterator<Map.Entry<Long, BigDecimal>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, BigDecimal> next = it.next();

            ckQty.stream()
                    .filter(item -> item.getAId().equals(next.getKey()))
                    .findFirst()
                    .ifPresent(item -> {
                        BigDecimal subtract = next.getValue().subtract(item.getQty());
                        if (subtract.compareTo(BigDecimal.ZERO) < 1) {
                            it.remove();
                        } else {
                            next.setValue(subtract);
                        }
                    });
        }

        return new AsyncResult<>(map);
    }

    @Async
    public Future<List<Long>> scJhInfo(List<MaterialInfo> infos, Set<Long> materialIds) {
        List<ProduceEntity> produces = this.produceService.lambdaQuery()
                .lt(ProduceEntity::getStatus, 2)
                .list();

        if (CollUtil.isEmpty(produces)) {
            return new AsyncResult<>(new ArrayList<>(0));
        }

        List<Long> planIds = new ArrayList<>(produces.size());
        MaterialInfo[] info = {null};
        for (ProduceEntity produce : produces) {
            Long productId = produce.getProductId();

            BomVo bom = this.proFromRedis.getBom(productId);
            ProductVo product = this.proFromRedis.getProduct(productId);
            if (ObjUtil.isNull(bom) || ObjUtil.isNull(product)) {
                continue;
            }

            List<BomMaterialVo> materialVos = bom.getMaterialVos()
                    .stream()
                    .filter(item -> materialIds.contains(item.getMaterialId()))
                    .collect(Collectors.toList());

            if (CollUtil.isEmpty(materialVos)) {
                continue;
            }

            BigDecimal yield = product.getYield().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal qty = produce.getProductQty();

            materialVos.forEach(item -> {
                info[0] = new MaterialInfo();
                info[0].setId(item.getMaterialId());
                info[0].setType(item.getType());
                info[0].setPreInventory(qty.multiply(item.getQty()).divide(yield, 0, RoundingMode.UP));
                info[0].setLevel(10);
                infos.add(info[0]);
            });

            planIds.add(produce.getId());
        }

        return new AsyncResult<>(planIds);
    }

    @Async
    public Future<List<MaterialInfo>> dealInfo(List<MaterialInfo> infos,
                                               List<Long> planIds,
                                               Map<Long, BigDecimal> map,
                                               boolean isProduct) {
        Set<Long> ids = this.distinct(infos, map, isProduct);
        if (CollUtil.isEmpty(ids)){
            return new AsyncResult<>(infos);
        }

        int i = isProduct ? 1 : 2;
        // 查询库存量
        List<CountVo> kcl = this.storageService.getKcl(ids, i);

        // 查询计划对应工单
        List<CountVo> vos = null;
        if (CollUtil.isNotEmpty(planIds)) {
            Set<String> codes = this.workOrderMxService.getByPlanIds(planIds);
            if (CollUtil.isNotEmpty(codes)) {
                // 查询工单出库数量
                vos = this.outboundMxService.getCkQty(codes, ids, 0, i);
            }
        }

        // 计算预占用库存
        for (MaterialInfo info : infos) {
            Long id = info.getId();

            if (vos != null && !vos.isEmpty()) {
                vos.stream()
                        .filter(item -> item.getAId().equals(id))
                        .findFirst()
                        .ifPresent(item -> {
                            BigDecimal subtract = info.getPreInventory().subtract(item.getQty());
                            if (subtract.compareTo(BigDecimal.ZERO) < 0) {
                                subtract = BigDecimal.ZERO;
                            }

                            info.setPreInventory(subtract);
                        });
            }

            if (CollUtil.isNotEmpty(kcl)) {
                kcl.stream()
                        .filter(item -> item.getAId().equals(id))
                        .findFirst()
                        .ifPresent(item -> info.setInventory(item.getQty()));
            }

            if (isProduct) {
                ProductVo product = this.proFromRedis.getProduct(id);
                if (ObjUtil.isNotNull(product)) {
                    info.setCode(product.getCode());
                    info.setTitle(product.getTitle());
                }
            } else {
                MaterialVo material = this.proFromRedis.getMaterial(id);
                if (ObjUtil.isNotNull(material)) {
                    info.setCode(material.getCode());
                    info.setTitle(material.getTitle());
                    info.setMinPackQty(material.getMinPackQty());
                    info.setMoq(material.getMoq());
                    info.setThresholdQty(material.getQty());
                    info.setPurchaseCycle(material.getPurchaseCycle());
                }
            }

            ProductVo product = this.proFromRedis.getProduct(info.getPid());
            info.setPCode(product.getCode());
            info.setPTitle(product.getTitle());
        }

        return new AsyncResult<>(infos);
    }

    private Set<Long> distinct(List<MaterialInfo> infos, Map<Long, BigDecimal> map, boolean isProduct) {
        infos.removeIf(item -> {
            if (isProduct) {
                return item.getType() == 0 || item.getLevel() == 2;
            } else {
                return item.getType() == 1;
            }
        });

        if (CollUtil.isEmpty(infos)) {
            return null;
        }

        Set<Long> ids = infos.stream()
                .map(MaterialInfo::getId)
                .collect(Collectors.toSet());

        // 需求量累加后删除重复的数据
        for (Long id : ids) {
            List<MaterialInfo> tmp = infos.stream()
                    .filter(item -> item.getId().equals(id))
                    .collect(Collectors.toList());

            MaterialInfo info = tmp.get(0);
            if (tmp.size() > 1) {
                BigDecimal[] qty = {BigDecimal.ZERO};
                BigDecimal[] sq = {BigDecimal.ZERO};

                tmp.forEach(item -> {
                    /*if (isProduct || item.getLevel() == 1) {
                        qty[0] = qty[0].add(item.getSingleQty());
                    } else {
                        BigDecimal multiply = item.getSingleQty().multiply(item.getPsingleQty());
                        qty[0] = qty[0].add(multiply);
                    }*/
                    if (item.getLevel() < 10) {
                        sq[0] = sq[0].add(item.getSingleQty());
                    }

                    qty[0] = qty[0].add(item.getPreInventory());
                });

                // BigDecimal multiply = qty[0].multiply(cpSum);
                BigDecimal bd = map.get(id);
                if (ObjUtil.isNotNull(bd)) {
                    qty[0] = qty[0].add(bd);
                }

                info.setSingleQty(sq[0]);
                info.setPreInventory(qty[0]);

                infos.removeAll(tmp);
                infos.add(info);
            } else {
                // BigDecimal multiply = info.getSingleQty().multiply(cpSum);
                BigDecimal preInventory = info.getPreInventory();
                BigDecimal bd = map.get(id);
                if (ObjUtil.isNotNull(bd)) {
                    preInventory = preInventory.add(bd);
                }

                info.setPreInventory(preInventory);
            }
        }

        return ids;
    }

    /**
     * 变更产线总量
     */
    /*@Async
    public void changeLineTotal(BigDecimal qty, ProduceEntity produce, char c) {
        ProductLineEntity productLine = this.productLineService.getByProductId(produce.getProductId());
        BigDecimal total;
        if ('I' == c) {
            total = productLine.getTotal().add(produce.getProductQty());
        } else if ('D' == c) {
            total = productLine.getTotal().subtract(produce.getProductQty());
        } else {
            BigDecimal subtract = productLine.getTotal().subtract(qty);
            total = subtract.add(produce.getProductQty());
        }

        this.productLineService.lambdaUpdate()
                .set(ProductLineEntity::getTotal, total)
                .eq(ProductLineEntity::getId, productLine.getId())
                .update();
    }*/

    /**
     * MRP创建生产计划批量变更产线总量
     */
    /*@Async
    public void changeLineTotalBatch(List<ProduceEntity> produces) {
        for (ProduceEntity produce : produces) {
            ProductLineEntity productLine = this.productLineService.getByProductId(produce.getProductId());
            BigDecimal total = productLine.getTotal().add(produce.getProductQty());

            this.productLineService.lambdaUpdate()
                    .set(ProductLineEntity::getTotal, total)
                    .eq(ProductLineEntity::getId, productLine.getId())
                    .update();
        }
    }*/

    /**
     * MRP删除批量变更产线总量
     */
    /*@Async
    public void changeLineTotalByMrpId(Long mrpId) {
        List<MrpProduct> list = this.mrpProductService.lambdaQuery()
                .eq(MrpProduct::getMrpId, mrpId)
                .eq(MrpProduct::getProductMode, 0)
                .list();

        for (MrpProduct product : list) {
            ProductLineEntity productLine = this.productLineService.getByProductId(product.getId());
            BigDecimal total = productLine.getTotal().subtract(product.getJianYiShengChanLiang());

            this.productLineService.lambdaUpdate()
                    .set(ProductLineEntity::getTotal, total)
                    .eq(ProductLineEntity::getId, productLine.getId())
                    .update();
        }

        this.mrpProductService.lambdaUpdate().eq(MrpProduct::getMrpId, mrpId).remove();
    }*/

}
