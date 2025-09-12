package com.senmol.mes.workorder.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.workorder.entity.*;
import com.senmol.mes.workorder.mapper.WorkOrderMxMapper;
import com.senmol.mes.workorder.service.*;
import com.senmol.mes.workorder.vo.StationInfo;
import com.senmol.mes.workorder.vo.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 工单明细(WorkOrderMx)表服务实现类
 *
 * @author makejava
 * @since 2023-02-20 11:03:10
 */
@Service("workOrderMxService")
public class WorkOrderMxServiceImpl extends ServiceImpl<WorkOrderMxMapper, WorkOrderMxEntity> implements WorkOrderMxService {

    @Resource
    private WorkOrderMxMaterialService workOrderMxMaterialService;
    @Resource
    private StorageInspectService storageInspectService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProduceService produceService;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private WorkOrderMaterialService workOrderMaterialService;
    @Resource
    private WorkOrderMxProcessService workOrderMxProcessService;
    @Resource
    private WorkOrderMxBadModeService workOrderMxBadModeService;
    @Resource
    private WorkOrderMxProcessUserService workOrderMxProcessUserService;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SaResult getByPid(Long pid) {
        List<WorkOrderMxEntity> list = this.lambdaQuery()
                .eq(WorkOrderMxEntity::getPid, pid)
                .orderByDesc(WorkOrderMxEntity::getCreateTime)
                .list();

        List<WorkOrderMxEntity> collect = list.stream()
                .filter(item -> item.getIsFree() == 0 && item.getCreateTime().isBefore(LocalDate.now().atStartOfDay()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            list.removeAll(collect);

            Date date = new Date();
            int count = Math.toIntExact(this.lambdaQuery()
                    .between(WorkOrderMxEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date))
                    .count());
            String format = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);

            int i = 0;
            LocalDateTime now = LocalDateTime.now();
            long userId = StpUtil.getLoginIdAsLong();
            for (WorkOrderMxEntity workOrderMx : collect) {
                int num = 101 + (count + i++) * 3;
                workOrderMx.setCode("ZGD" + format + num);
                workOrderMx.setCreateTime(now);
                workOrderMx.setCreateUser(userId);
                workOrderMx.setUpdateTime(now);
                workOrderMx.setUpdateUser(userId);
                list.add(i - 1, workOrderMx);
            }

            this.updateBatchById(collect);
        }

        return SaResult.data(list);
    }

    @Override
    public List<WorkOrderMxEntity> getByPlanId(Long planId) {
        return this.baseMapper.getByPlanId(planId);
    }

    @Override
    public SaResult getByCode(String code) {
        Long pid = this.lambdaQuery().eq(WorkOrderMxEntity::getCode, code).one().getPid();
        ProduceEntity produce = this.produceService.getById(pid);
        if (ObjectUtil.isNull(produce)) {
            return SaResult.ok();
        }

        Long productId = produce.getProductId();
        ProductVo product = this.proFromRedis.getProduct(productId);

        Dict dict = Dict.create()
                .set("id", productId)
                .set("code", ObjUtil.isNull(product) ? "" : product.getCode())
                .set("productQty", produce.getProductQty());
        return SaResult.data(dict);
    }

    @Override
    public List<WorkOrderMxDto> getLineOrder(Long productLine, Long planId) {
        List<WorkOrderMxDto> lineOrder = this.baseMapper.getLineOrder(productLine, planId);
        for (WorkOrderMxDto workOrderMx : lineOrder) {
            workOrderMx.setYield(workOrderMx.getNonDefective()
                    .divide(workOrderMx.getQty(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)));

            ProductVo product = this.proFromRedis.getProduct(workOrderMx.getProductId());
            if (ObjUtil.isNotNull(product)) {
                workOrderMx.setProductTitle(product.getTitle());
                workOrderMx.setProductCode(product.getCode());
            }
        }

        return lineOrder;
    }

    @Override
    public StationInfo getStationInfo(String code) {
        StationInfo info = this.baseMapper.getByMxCode(code);
        if (info == null) {
            return null;
        }

        ProductVo product = this.proFromRedis.getProduct(info.getProductId());
        if (ObjUtil.isNotNull(product)) {
            info.setProductCode(product.getCode());
        }
        return info;
    }

    @Override
    public List<MaterialPojo> getMaterials(String code, Long stationId) {
        List<MaterialPojo> list = this.baseMapper.getMaterials(code, stationId);

        for (MaterialPojo pojo : list) {
            if (pojo.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(pojo.getMaterialId());
                if (ObjUtil.isNotNull(material)) {
                    pojo.setMaterialCode(material.getCode());
                    pojo.setMaterialTitle(material.getTitle());
                    pojo.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(pojo.getMaterialId());
                if (ObjUtil.isNotNull(product)) {
                    pojo.setMaterialCode(product.getCode());
                    pojo.setMaterialTitle(product.getTitle());
                    pojo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            }
        }

        return list;
    }

    @Override
    public List<OrderMaterialInfo> retrospect(String code) {
        List<OrderMaterialInfo> infos = this.baseMapper.retrospect(code);

        infos.stream()
                .filter(Objects::nonNull)
                .forEach(item -> {
                    if (item.getType() > 1) {
                        MaterialVo material = this.proFromRedis.getMaterial(item.getMaterialId());
                        if (ObjUtil.isNotNull(material)) {
                            item.setMaterialCode(material.getCode());
                            item.setMaterialTitle(material.getTitle());
                        }
                    } else {
                        ProductVo product = this.proFromRedis.getProduct(item.getMaterialId());
                        if (ObjUtil.isNotNull(product)) {
                            item.setMaterialCode(product.getCode());
                            item.setMaterialTitle(product.getTitle());
                        }
                    }

                    item.setICreateUserName(this.sysFromRedis.getUser(item.getICreateUser()));
                    item.setOCreateUserName(this.sysFromRedis.getUser(item.getOCreateUser()));
                });

        return infos;
    }

    @Override
    public List<ProcessEntity> getProcesses(String mxCode) {
        List<ProcessEntity> processes = this.baseMapper.getProcesses(mxCode);
        processes.forEach(item -> {
            ProcessVo process = this.proFromRedis.getProcess(item.getId());
            if (ObjUtil.isNotNull(process)) {
                item.setTitle(process.getTitle());
            }
        });

        return processes;
    }

    @Override
    public ProductLineEntity getLineInfo(Long mxId) {
        return this.baseMapper.getLineInfo(mxId);
    }

    @Override
    public BigDecimal getStorageQty() {
        return this.baseMapper.getStorageQty();
    }

    @Override
    public BigDecimal getSumQty() {
        return this.baseMapper.getSumQty();
    }

    @Override
    public Set<String> getByPlanIds(List<Long> planIds) {
        return this.baseMapper.getByPlanIds(planIds);
    }

    @Override
    public SaResult insert(WorkOrderMxEntity workOrderMx) {
        Date date = new Date();
        Long count = this.lambdaQuery()
                .between(WorkOrderMxEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date))
                .count();

        workOrderMx.setCode("ZGD" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + count * 3));
        this.save(workOrderMx);

        this.produceService.lambdaUpdate()
                .setSql("total = total + 1")
                .eq(ProduceEntity::getId, workOrderMx.getPid())
                .update();

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult insertBatch(List<WorkOrderMxEntity> workOrderMxs) {
        Date date = new Date();
        int count = Math.toIntExact(this.lambdaQuery()
                .between(WorkOrderMxEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date))
                .count());
        String format = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);

        int i = 0;
        for (WorkOrderMxEntity workOrderMx : workOrderMxs) {
            int num = 101 + (count + i++) * 3;
            workOrderMx.setCode("ZGD" + format + num);
        }

        boolean b = this.saveBatch(workOrderMxs);
        if (b) {
            this.produceService.lambdaUpdate()
                    .set(ProduceEntity::getTotal, workOrderMxs.size())
                    .eq(ProduceEntity::getId, workOrderMxs.get(0).getPid())
                    .update();
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateWorkOrderMx(WorkOrderMxEntity workOrderMx) {
        WorkOrderMxEntity entity = this.getById(workOrderMx.getId());
        if (BeanUtil.isEmpty(entity)) {
            return SaResult.error("数据不存在");
        }

        boolean b = this.updateById(workOrderMx);
        if (b) {
            // 释放完才能打印，第一次打印计算产线产量
            if (ObjUtil.isNotNull(workOrderMx.getStatus()) && entity.getStatus() == 0 && workOrderMx.getStatus() == 1) {
                Long pid = entity.getPid();
                ProduceEntity produce = this.produceService.getById(pid);
                // 变更产线信息
                this.changLineRate(produce.getProductId(), entity.getQty(), true);
                this.produceService.lambdaUpdate().set(ProduceEntity::getPrinted, produce.getPrinted() + 1)
                        .eq(ProduceEntity::getId, pid).update();
            }

            // 终止工单，finish=1、status=3，产线总量扣除对应数量，完成率重新计算
            if (ObjUtil.isNotNull(workOrderMx.getFinish()) && entity.getFinish() == 0 && workOrderMx.getFinish() == 1 &&
                    ObjUtil.isNotNull(workOrderMx.getStatus()) && workOrderMx.getStatus() == 3
            ) {
                Long pid = entity.getPid();
                ProduceEntity produce = this.produceService.getById(pid);
                // 变更产线信息
                this.changLineRate(produce.getProductId(), entity.getQty(), false);
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateCutoff(Long id) {
        WorkOrderMxEntity mx = this.getById(id);
        if (ObjUtil.isNull(mx)) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        Long count = this.workOrderMxProcessService.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, id)
                .eq(WorkOrderMxProcess::getStatus, 2)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("工单已报工");
        }

        boolean b = this.lambdaUpdate().set(WorkOrderMxEntity::getStatus, 2).eq(WorkOrderMxEntity::getId, id).update();
        if (b) {
            try {
                CompletableFuture.allOf(
                        CompletableFuture.runAsync(() -> this.changeProduce(mx), this.executor),
                        CompletableFuture.runAsync(() -> this.delMxInfo(id), this.executor)
                ).join();
            } catch (Exception e) {
                throw new BusinessException("工单终止失败，任务单信息变更异常");
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateByIdAndSubmit(WorkOrderMxEntity workOrderMx) {
        WorkOrderMxEntity entity = this.getById(workOrderMx.getId());
        if (entity == null) {
            return SaResult.error("工单不存在");
        }

        if (entity.getFinish() == 0) {
            return SaResult.error("工单未完成");
        }

        if (entity.getSubmit() == 1) {
            return SaResult.error("工单已送检");
        }

        entity.setSubmit(1);
        this.updateById(entity);

        ProduceEntity produce = this.produceService.getById(entity.getPid());
        Long productId = produce.getProductId();
        ProductVo product = this.proFromRedis.getProduct(productId);
        // 送检
        StorageInspectEntity inspect = new StorageInspectEntity();
        inspect.setPid(entity.getPid());
        inspect.setReceiptCode(entity.getCode());
        inspect.setBatchNo(entity.getCode());
        inspect.setCensorshipQty(workOrderMx.getNonDefective());
        inspect.setGoodsId(productId);
        inspect.setDetectionWay(1);
        inspect.setSource(2);

        if (ObjUtil.isNotNull(product)) {
            inspect.setType(product.getType() == 0 ? 1 : 0);
        }
        this.storageInspectService.insertStorageInspect(inspect);

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteWorkOrderMx(Long id) {
        WorkOrderMxEntity workOrderMx = this.getById(id);
        Integer status = workOrderMx.getStatus();
        if (status > 0) {
            return SaResult.error((status == 1 ? "工单已确认" : "工单已终止") + "，不允许删除");
        }

        boolean b = this.removeById(id);
        if (b) {
            // 删除工单工序
            this.workOrderMxProcessService.lambdaUpdate().eq(WorkOrderMxProcess::getMxId, id).remove();
            // 删除工单明细物料数据
            this.workOrderMxMaterialService.lambdaUpdate().eq(WorkOrderMxMaterialEntity::getMxId, id).remove();

            Long pid = workOrderMx.getPid();
            List<WorkOrderMxEntity> list = this.lambdaQuery().eq(WorkOrderMxEntity::getPid, pid).list();
            long count = list.stream()
                    .filter(item -> item.getIsFree() == 1)
                    .count();

            int size = list.size();
            int isFree = 0;
            if (count == size) {
                isFree = 1;
            } else if (count > 0) {
                isFree = 2;
            }

            this.produceService.lambdaUpdate()
                    .setSql("total = total - 1")
                    .set(ProduceEntity::getIsFree, isFree)
                    .eq(ProduceEntity::getId, pid)
                    .update();

            if (isFree == 0) {
                this.workOrderMaterialService.lambdaUpdate()
                        .eq(WorkOrderMaterial::getMxId, id)
                        .remove();
            }
        }

        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    @Override
    public SaResult getByLineId(Long productLineId) {
        List<ProductPojo> list = this.baseMapper.getByLineId(productLineId);
        Set<Object> set = new HashSet<>((int) (list.size() / 0.75 + 1));

        List<ProductPojo> pojos = new ArrayList<>(list.size());
        for (ProductPojo pojo : list) {
            boolean add = set.add(pojo.getId());
            if (!add) {
                continue;
            }

            ProductVo product = this.proFromRedis.getProduct(pojo.getProductId());
            if (ObjUtil.isNotNull(product)) {
                pojo.setProductCode(product.getCode());
                pojo.setProductTitle(product.getTitle());
            }

            StationVo station = this.proFromRedis.getStation(pojo.getStationId());
            if (ObjUtil.isNotNull(station)) {
                pojo.setStationTitle(station.getTitle());
            }

            pojos.add(pojo);
        }

        return SaResult.data(pojos);
    }

    @Override
    public SaResult workbench(Long productLineId) {
        List<ProductLineInfo> orders = this.produceService.statOrder(productLineId);
        List<ProductLineInfo> orderMxs = this.baseMapper.statOrderMx(productLineId);

        for (ProductLineInfo order : orders) {
            List<Long> longs = Convert.toList(Long.class, order.getOrderIds());

            List<ProductLineInfo> collect = orderMxs.stream()
                    .filter(item -> longs.contains(item.getId()))
                    .collect(Collectors.toList());

            collect.forEach(item -> {
                order.setOrderQty(order.getOrderQty().add(item.getOrderQty()));
                order.setNonDefective(order.getNonDefective().add(item.getNonDefective()));
                order.setDefective(order.getDefective().add(item.getDefective()));
            });

            LineVo line = this.proFromRedis.getLine(order.getId());
            if (ObjUtil.isNotNull(line)) {
                order.setCode(line.getCode());
                order.setTitle(line.getTitle());
            }

            ProductVo product = this.proFromRedis.getProduct(order.getProductId());
            if (ObjUtil.isNotNull(product)) {
                order.setProductCode(product.getCode());
                order.setProductTitle(product.getTitle());
            }
        }

        return SaResult.data(orders);
    }

    @Override
    public List<StatInfo> workbenchStat(Integer wom, Long productLineId) {
        Date date = new Date();
        List<StatInfo> list = this.baseMapper.getStatByWeek(wom, productLineId);

        List<String> ids = list.stream()
                .map(StatInfo::getIds)
                .filter(Objects::nonNull)
                .flatMap(idsStr -> Arrays.stream(idsStr.split(",")))
                .collect(Collectors.toList());

        if (ids.size() > 0) {
            List<StatInfo> infos = this.baseMapper.getStatYield(wom, ids);
            Map<Integer, StatInfo> yieldMap = infos.stream().collect(Collectors.toMap(StatInfo::getDate, v -> v));

            list.forEach(info -> {
                StatInfo si = yieldMap.get(info.getDate());
                if (si == null) {
                    info.setYield(BigDecimal.ZERO);
                } else {
                    info.setYield(si.getSQty().divide(si.getPQty(), 4, RoundingMode.HALF_UP));
                    info.setQty(si.getSQty());
                }
            });
        }

        int i;
        // 周统计
        if (wom == 0) {
            i = DateUtil.weekOfYear(date);
        } else {
            // 月统计
            i = DateUtil.month(date) + 1;
        }

        if (list.size() < i) {
            this.makeUp(list, i);
        }

        return list.stream().sorted(Comparator.comparing(StatInfo::getDate)).collect(Collectors.toList());
    }

    private void makeUp(List<StatInfo> list, int count) {
        for (int i = 1; i <= count; i++) {
            final int tmp = i;
            StatInfo statInfo = list.stream()
                    .filter(item -> item.getDate() == tmp)
                    .findFirst()
                    .orElse(null);

            if (statInfo == null) {
                list.add(new StatInfo(i, BigDecimal.ZERO, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            }
        }
    }

    private void changeProduce(WorkOrderMxEntity mx) {
        Long pid = mx.getPid();
        List<WorkOrderMxEntity> list =
                this.lambdaQuery().eq(WorkOrderMxEntity::getPid, pid).ne(WorkOrderMxEntity::getStatus, 2).list();

        int size = list.size();
        long count = list.stream().filter(item -> item.getIsFree() == 1).count();

        LambdaUpdateChainWrapper<ProduceEntity> wrapper = this.produceService.lambdaUpdate();
        if (count == 0L) {
            wrapper.set(ProduceEntity::getIsFree, 0);
        } else if (count == size) {
            wrapper.set(ProduceEntity::getIsFree, 1);
        } else {
            wrapper.set(ProduceEntity::getIsFree, 2);
        }

        ProduceEntity produce = this.produceService.getById(pid);
        if (mx.getStatus() == 1) {
            int i = produce.getPrinted() - 1;
            if (i == 0) {
                wrapper.set(ProduceEntity::getStatus, 0);
            }

            wrapper.set(ProduceEntity::getPrinted, i)
                    .set(ProduceEntity::getTotal, produce.getTotal() - 1)
                    .eq(ProduceEntity::getId, pid)
                    .update();

            // 变更产线信息
            this.changLineRate(produce.getProductId(), mx.getQty(), false);
        }
    }

    private void changLineRate(Long productId, BigDecimal qty, boolean flag) {
        BomVo bom = this.proFromRedis.getBom(productId);
        ProductLineEntity productLine = this.productLineService.getById(bom.getProductLineId());
        BigDecimal lineTotal = productLine.getTotal();
        BigDecimal total = flag ? lineTotal.add(qty) : lineTotal.subtract(qty);
        BigDecimal rate;
        if (!flag && total.compareTo(BigDecimal.ZERO) < 1) {
            total = BigDecimal.ZERO;
            rate = BigDecimal.ZERO;
        } else {
            rate = lineTotal.multiply(productLine.getRate()).divide(total, 4, RoundingMode.HALF_UP);
        }

        this.productLineService.lambdaUpdate()
                .set(ProductLineEntity::getTotal, total)
                .set(ProductLineEntity::getRate, rate)
                .eq(ProductLineEntity::getId, productLine.getId())
                .update();
    }

    private void delMxInfo(Long id) {
        WorkOrderMxProcess process = this.workOrderMxProcessService.lambdaQuery()
                .eq(WorkOrderMxProcess::getMxId, id)
                .orderByAsc(WorkOrderMxProcess::getSerialNo)
                .last(CheckToolUtil.LIMIT)
                .one();

        if (ObjUtil.isNotNull(process)) {
            this.workOrderMxBadModeService.lambdaUpdate()
                    .eq(WorkOrderMxBadMode::getMxId, id)
                    .eq(WorkOrderMxBadMode::getProcessId, process.getProcessId())
                    .remove();

            Long processId = process.getId();
            this.workOrderMxProcessService.lambdaUpdate()
                    .eq(WorkOrderMxProcess::getId, processId)
                    .remove();

            this.workOrderMxProcessUserService.lambdaUpdate()
                    .in(WorkOrderMxProcessUser::getMxProcessId, processId)
                    .remove();
        }
    }

}

