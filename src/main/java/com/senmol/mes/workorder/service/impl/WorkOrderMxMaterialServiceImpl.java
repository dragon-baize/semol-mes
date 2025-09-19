package com.senmol.mes.workorder.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.common.utils.OutboundMaterial;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.service.BomService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.utils.WarAsyncUtil;
import com.senmol.mes.warehouse.vo.StockVo;
import com.senmol.mes.workorder.entity.WorkOrderMaterial;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxMaterialEntity;
import com.senmol.mes.workorder.mapper.WorkOrderMxMaterialMapper;
import com.senmol.mes.workorder.service.WorkOrderMaterialService;
import com.senmol.mes.workorder.service.WorkOrderMxMaterialService;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import com.senmol.mes.workorder.utils.WorkOrderAsyncUtil;
import com.senmol.mes.workorder.vo.MaterialPojo;
import com.senmol.mes.workorder.vo.OrderInfo;
import com.senmol.mes.workorder.vo.OrderMaterial;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 工单明细物料(WorkOrderMxMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-02-21 10:54:03
 */
@Service("workOrderMxMaterialService")
public class WorkOrderMxMaterialServiceImpl extends ServiceImpl<WorkOrderMxMaterialMapper, WorkOrderMxMaterialEntity> implements WorkOrderMxMaterialService {

    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private WarAsyncUtil warAsyncUtil;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private BomService bomService;
    @Lazy
    @Resource
    private WorkOrderAsyncUtil workOrderAsyncUtil;
    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private ProduceService produceService;
    @Resource
    private WorkOrderMxMaterialService workOrderMxMaterialService;
    @Resource
    private WorkOrderMaterialService workOrderMaterialService;
    @Resource
    private StorageService storageService;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public List<WorkOrderMxMaterialEntity> getByMxId(Long workOrderMxId) {
        List<WorkOrderMxMaterialEntity> list =
                this.lambdaQuery().eq(WorkOrderMxMaterialEntity::getMxId, workOrderMxId).list();

        for (WorkOrderMxMaterialEntity entity : list) {
            if (entity.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(entity.getMaterialId());
                if (ObjUtil.isNotNull(material)) {
                    entity.setMaterialTitle(material.getTitle());
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(entity.getMaterialId());
                if (ObjUtil.isNotNull(product)) {
                    entity.setMaterialTitle(product.getTitle());
                }
            }
        }

        return list;
    }

    @Override
    public SaResult getByMxCode(String mxCode) {
        List<OutboundMaterial> materials2 = this.baseMapper.getMaterials(mxCode);
        materials2.forEach(item -> {
            // 物料
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

        return SaResult.data(materials2);
    }

    @Override
    public List<OrderMaterial> getMaterials(Long productId, Long id, Long mxId) {
        List<OrderMaterial> list;
        if (ObjUtil.isNull(mxId)) {
            list = this.convertBomMaterial(productId);
        } else {
            List<WorkOrderMxMaterialEntity> mxMaterials = this.workOrderMxMaterialService.lambdaQuery()
                    .eq(WorkOrderMxMaterialEntity::getMxId, mxId)
                    .list();

            if (mxMaterials.isEmpty()) {
                list = this.convertBomMaterial(productId);
            } else {
                list = this.convertOrderMaterial(mxMaterials);
            }
        }

        Set<Long> goodsIds = list.stream().map(OrderMaterial::getId).collect(Collectors.toSet());
        List<CountVo> byGoodsIds = this.storageService.getByGoodsIds(goodsIds);

        List<CountVo> vos = Collections.emptyList();
        if (ObjUtil.isNotNull(mxId)) {
            WorkOrderMxEntity workOrderMx = this.workOrderMxService.getById(mxId);
            vos = this.retrievalMxService.getByGdCode(workOrderMx.getCode());
        }

        for (OrderMaterial info : list) {
            Long goodsId = info.getId();

            if (CollUtil.isNotEmpty(byGoodsIds)) {
                byGoodsIds.stream()
                        .filter(item -> goodsId.equals(item.getAId()))
                        .findFirst()
                        .ifPresent(item -> info.setStorageQty(item.getQty()));
            }

            if (CollUtil.isNotEmpty(vos)) {
                vos.stream()
                        .filter(item -> goodsId.equals(item.getAId()))
                        .findFirst()
                        .ifPresent(item -> info.setActQty(item.getQty()));
            }

            if (info.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(goodsId);
                if (ObjUtil.isNotNull(material)) {
                    info.setCode(material.getCode());
                    info.setTitle(material.getTitle());
                    info.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(goodsId);
                if (ObjUtil.isNotNull(product)) {
                    info.setCode(product.getCode());
                    info.setTitle(product.getTitle());
                    info.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            }

            ProcessVo process = this.proFromRedis.getProcess(info.getProcessId());
            if (ObjUtil.isNotNull(process)) {
                info.setProcessTitle(process.getTitle());
            }
        }

        return list;
    }

    @Override
    public List<OrderMaterial> selectByMxId(Long mxId) {
        List<Long> materialIds = this.baseMapper.getValidMaterial(mxId);
        List<OrderMaterial> infos = this.baseMapper.selectByMxId(mxId, materialIds);

        for (OrderMaterial info : infos) {
            if (info.getType() > 1) {
                com.senmol.mes.produce.vo.MaterialVo material = this.proFromRedis.getMaterial(info.getId());
                if (ObjUtil.isNotNull(material)) {
                    info.setCode(material.getCode());
                    info.setTitle(material.getTitle());
                    info.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(info.getId());
                if (ObjUtil.isNotNull(product)) {
                    info.setCode(product.getCode());
                    info.setTitle(product.getTitle());
                    info.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            }

            StockVo stockVo = this.warAsyncUtil.getStock(info.getStockId());
            if (ObjUtil.isNotNull(stockVo)) {
                info.setStockTitle(stockVo.getTitle());
            }
        }

        return infos;
    }

    @Override
    public SaResult preInventory(Long materialId) {
        // 物料对应清单产品
        List<CountVo> vos = this.bomService.getProductId(materialId);

        // 处理委外计划
        Future<List<MaterialPojo>> wwJh = this.workOrderAsyncUtil.dealWwJh(vos, materialId);
        // 处理产品计划
        Future<List<MaterialPojo>> scJh = this.workOrderAsyncUtil.dealScJh(vos, materialId);

        try {
            List<MaterialPojo> wwList = wwJh.get();
            List<MaterialPojo> scList = scJh.get();

            wwList.addAll(scList);
            return SaResult.data(wwList);
        } catch (InterruptedException | ExecutionException e) {
            throw new BusinessException("库存计算出错，请重试！");
        }
    }

    @Override
    public List<CountVo> getMaterial(Set<Long> ids) {
        return this.baseMapper.getMaterial(ids);
    }

    @Override
    public SaResult retrospect(String batchNo) {
        // 查询工单信息
        List<OrderInfo> orderInfos = this.baseMapper.retrospect(batchNo);
        if (CollUtil.isEmpty(orderInfos)) {
            return SaResult.error("工单数据不存在");
        }

        // 产线查产品工艺
        orderInfos.forEach(item -> {
            ProductVo product = this.proFromRedis.getProduct(item.getProductId());
            if (ObjUtil.isNotNull(product)) {
                item.setProductCode(product.getCode());
                item.setProductTitle(product.getTitle());
            }

            WorkmanshipVo workmanship = this.proFromRedis.getWorkmanship(item.getWorkmanshipId() + ":v" + item.getWmsVersion());
            if (ObjUtil.isNotNull(workmanship)) {
                item.setWorkmanshipTitle(workmanship.getTitle());
            }

            item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser()));
        });

        return SaResult.data(orderInfos);
    }

    @Override
    public SaResult insertBatch(List<WorkOrderMxMaterialEntity> workOrderMxMaterials) {
        Long mxId = workOrderMxMaterials.get(0).getMxId();
        this.lambdaUpdate().eq(WorkOrderMxMaterialEntity::getMxId, mxId).remove();

        boolean b = this.saveBatch(workOrderMxMaterials);
        if (b) {
            WorkOrderMxEntity workOrderMx = this.workOrderMxService.getById(mxId);
            Long pid = workOrderMx.getPid();

            try {
                CompletableFuture.allOf(
                        CompletableFuture.runAsync(() -> this.createWorkOrderMaterial(mxId, pid), this.executor),
                        CompletableFuture.runAsync(() -> this.changeProduce(workOrderMx.getIsFree(), mxId, pid), this.executor)
                ).join();
            } catch (Exception e) {
                throw new BusinessException(e.getMessage());
            }
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    private List<OrderMaterial> convertBomMaterial(Long productId) {
        BomVo bom = this.proFromRedis.getBom(productId);
        if (bom == null) {
            throw new BusinessException("产品物料清单不存在");
        }

        List<BomMaterialVo> materialVos = bom.getMaterialVos();
        materialVos.removeIf(item -> ObjUtil.isNull(item.getMaterialId()));

        List<OrderMaterial> list = new ArrayList<>(materialVos.size());
        OrderMaterial material;
        for (BomMaterialVo vo : materialVos) {
            material = new OrderMaterial();
            material.setId(vo.getMaterialId());
            material.setType(vo.getType() == 0 ? 2 : 1);
            material.setQty(vo.getQty());
            material.setStationId(vo.getStationId());
            material.setProcessId(vo.getProcessId());

            list.add(material);
        }

        return list;
    }

    private List<OrderMaterial> convertOrderMaterial(List<WorkOrderMxMaterialEntity> materialVos) {
        List<OrderMaterial> list = new ArrayList<>(materialVos.size());
        OrderMaterial material;
        for (WorkOrderMxMaterialEntity entity : materialVos) {
            material = new OrderMaterial();
            material.setId(entity.getMaterialId());
            material.setType(entity.getType());
            material.setQty(entity.getQty());
            material.setReceiveQty(entity.getReceiveQty());
            material.setStationId(entity.getStationId());
            material.setProcessId(entity.getProcessId());

            list.add(material);
        }

        return list;
    }

    private void createWorkOrderMaterial(Long mxId, Long pid) {
        Long count = this.workOrderMaterialService.lambdaQuery().eq(WorkOrderMaterial::getMxId, mxId).count();
        if (count == 0L) {
            ProduceEntity produce = this.produceService.lambdaQuery()
                    .eq(ProduceEntity::getId, pid)
                    .last(CheckToolUtil.LIMIT)
                    .one();

            BomVo bom = this.proFromRedis.getBom(produce.getProductId());
            if (bom == null) {
                throw new BusinessException("产品物料清单不存在");
            }

            List<BomMaterialVo> materialVos = bom.getMaterialVos();
            materialVos.forEach(item -> {
                if (ObjUtil.isNotNull(item.getType()) && item.getType() == 0) {
                    item.setType(2);
                }
            });

            int row = this.workOrderMaterialService.insertBatch(mxId, materialVos);
            if (row == 0) {
                throw new BusinessException("清单物料获取失败，请重试");
            }
        }
    }

    private void changeProduce(Integer free, Long mxId, Long pid) {
        if (free == 0) {
            // 生成工单编号
            Date date = new Date();
            Long total = this.workOrderMxService.lambdaQuery().eq(WorkOrderMxEntity::getIsFree, 1)
                    .between(WorkOrderMxEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date))
                    .count();
            String code = "ZGD" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + String.format("%03d", ++total);

            this.workOrderMxService.lambdaUpdate().set(WorkOrderMxEntity::getCode, code)
                    .set(WorkOrderMxEntity::getIsFree, 1).eq(WorkOrderMxEntity::getId, mxId).update();

            List<WorkOrderMxEntity> list = this.workOrderMxService.lambdaQuery().eq(WorkOrderMxEntity::getPid, pid)
                    .list();

            long count = list.stream().filter(item -> item.getIsFree() == 1).count();

            int size = list.size();
            int isFree = 0;
            if (count == size) {
                isFree = 1;
            } else if (count > 0) {
                isFree = 2;
            }

            boolean b = this.produceService.lambdaUpdate()
                    .set(ProduceEntity::getIsFree, isFree)
                    .eq(ProduceEntity::getId, pid)
                    .update();
            if (!b) {
                throw new BusinessException("任务单释放状态变更失败，请重试");
            }
        }
    }

}

