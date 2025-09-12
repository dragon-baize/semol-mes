package com.senmol.mes.workorder.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.service.OutboundMxService;
import com.senmol.mes.plan.service.OutsourceService;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.entity.WorkOrderMxProcess;
import com.senmol.mes.workorder.service.WorkOrderMxBadModeService;
import com.senmol.mes.workorder.service.WorkOrderMxProcessService;
import com.senmol.mes.workorder.service.WorkOrderMxProcessUserService;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import com.senmol.mes.workorder.vo.MaterialPojo;
import com.senmol.mes.workorder.vo.WorkOrderPojo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class WorkOrderAsyncUtil {

    @Resource
    private RedisService redisService;
    @Resource
    private ProduceService produceService;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private WorkOrderMxBadModeService workOrderMxBadModeService;
    @Resource
    private WorkOrderMxProcessService workOrderMxProcessService;
    @Resource
    private WorkOrderMxProcessUserService workOrderMxProcessUserService;
    @Resource
    private OutsourceService outsourceService;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private ProFromRedis proFromRedis;

    @Async
    @Transactional(rollbackFor = BusinessException.class)
    public CompletableFuture<Future<Void>> updateOrderPlan(Long mxId) {
        // 更新工单开工时间
        this.workOrderMxService.lambdaUpdate()
                .set(WorkOrderMxEntity::getBeginTime, LocalDateTime.now())
                .eq(WorkOrderMxEntity::getId, mxId)
                .update();

        // 更新计划状态为生产中
        WorkOrderPojo workOrder = this.getPlanId(mxId);
        if (BeanUtil.isNotEmpty(workOrder) && workOrder.getExist() == 0) {
            this.produceService.lambdaUpdate()
                    .set(ProduceEntity::getStatus, 1)
                    .eq(ProduceEntity::getId, workOrder.getId())
                    .update();

            workOrder.setExist(1);
            this.redisService.set(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + mxId, workOrder);
        }

        return CompletableFuture.completedFuture(null);
    }

    public void resetPlan(Long mxId) {
        WorkOrderPojo workOrder = this.getPlanId(mxId);
        // 查询是否存在已开工的任务单
        long count = this.workOrderMxProcessService.countBeginOrder(workOrder.getId());
        if (count < 1L) {
            // 生产计划的任务单都为开工，重置生产计划状态为待生产
            if (BeanUtil.isNotEmpty(workOrder)) {
                this.produceService.lambdaUpdate()
                        .set(ProduceEntity::getStatus, 0)
                        .eq(ProduceEntity::getId, workOrder.getId())
                        .update();
            }
        }

        this.redisService.del(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + mxId);
    }

    @Async
    public CompletableFuture<Future<Void>> updateWorkOrderMx(int indexOf, int size, WorkOrderMxProcess process) {
        // 最后一道工序更新工单数据
        if (indexOf == size - 1) {
            this.workOrderMxService.lambdaUpdate()
                    .set(WorkOrderMxEntity::getEndTime, LocalDateTime.now())
                    .set(WorkOrderMxEntity::getFinish, 1)
                    .set(WorkOrderMxEntity::getNonDefective, process.getNonDefective().add(process.getRework()))
                    .set(WorkOrderMxEntity::getDefective, process.getDefective())
                    .set(WorkOrderMxEntity::getYield, process.getYield())
                    .set(WorkOrderMxEntity::getUpdateTime, process.getUpdateTime())
                    .eq(WorkOrderMxEntity::getId, process.getMxId())
                    .update();

            this.redisService.del(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + process.getMxId());
        } else {
            this.workOrderMxService.lambdaUpdate()
                    .set(WorkOrderMxEntity::getNonDefective, process.getNonDefective().add(process.getRework()))
                    .set(WorkOrderMxEntity::getDefective, process.getDefective())
                    .set(WorkOrderMxEntity::getYield, process.getYield())
                    .set(WorkOrderMxEntity::getUpdateTime, process.getUpdateTime())
                    .eq(WorkOrderMxEntity::getId, process.getMxId())
                    .update();
        }

        return CompletableFuture.completedFuture(null);
    }

    private WorkOrderPojo getPlanId(Long mxId) {
        Object object = this.redisService.get(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + mxId);
        if (ObjUtil.isNotNull(object)) {
            return Convert.convert(WorkOrderPojo.class, object);
        }

        WorkOrderPojo workOrder = this.produceService.getByMxId(mxId);
        this.redisService.set(RedisKeyEnum.PLAN_PRODUCE_ID.getKey() + mxId, workOrder);
        return workOrder;
    }

    @Async
    public Future<List<MaterialPojo>> dealWwJh(List<CountVo> vos, Long materialId) {
        List<Long> productIds = vos.stream()
                .map(CountVo::getAId)
                .collect(Collectors.toList());

        // 查询未完成委外计划
        List<OutsourceEntity> outsources = this.outsourceService.getByCpId(productIds);
        if (CollUtil.isEmpty(outsources)) {
            return new AsyncResult<>(new ArrayList<>(0));
        }

        Set<String> codes = outsources.stream()
                .map(OutsourceEntity::getCode)
                .collect(Collectors.toSet());

        List<CountVo> ckQty = this.outboundMxService.getCkQty(codes, materialId, 1);
        ckQty.remove(null);
        List<MaterialPojo> list = new ArrayList<>(outsources.size());

        MaterialPojo pojo;
        for (OutsourceEntity outsource : outsources) {
            pojo = new MaterialPojo();

            Long productId = outsource.getProductId();
            ProductVo product = this.proFromRedis.getProduct(productId);
            if (ObjUtil.isNull(product)) {
                continue;
            }

            CountVo vo = vos.stream()
                    .filter(item -> item.getAId().equals(productId))
                    .findFirst()
                    .orElse(null);
            if (ObjUtil.isNull(vo)) {
                continue;
            }

            BigDecimal yield = product.getYield().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal qty = outsource.getQty().multiply(vo.getQty()).divide(yield, 0, RoundingMode.UP);
            String code = outsource.getCode();
            if (CollUtil.isNotEmpty(ckQty)) {
                CountVo ck = ckQty.stream()
                        .filter(item -> item.getAStr().equals(code))
                        .findFirst()
                        .orElse(null);

                if (ObjUtil.isNotNull(ck)) {
                    qty = qty.subtract(ck.getQty());
                }
            }

            if (qty.compareTo(BigDecimal.ZERO) < 1) {
                continue;
            }

            pojo.setSaleOrderCode(outsource.getOrderCode());
            pojo.setPlanCode(code);
            pojo.setProductCode(product.getCode());
            pojo.setProductTitle(product.getTitle());
            pojo.setReceiveQty(qty);
            list.add(pojo);
        }

        return new AsyncResult<>(list);
    }

    @Async
    public Future<List<MaterialPojo>> dealScJh(List<CountVo> vos, Long materialId) {
        List<Long> productIds = vos.stream()
                .map(CountVo::getAId)
                .collect(Collectors.toList());

        // 查询未完成生产计划
        List<ProduceEntity> produces = this.produceService.lambdaQuery()
                .in(ProduceEntity::getProductId, productIds)
                .lt(ProduceEntity::getStatus, 2)
                .list();

        if (CollUtil.isEmpty(produces)) {
            return new AsyncResult<>(new ArrayList<>(0));
        }

        List<Long> ids = produces.stream()
                .map(ProduceEntity::getId)
                .collect(Collectors.toList());

        Set<String> codes = this.workOrderMxService.getByPlanIds(ids);
        List<CountVo> ckQty = null;
        if (CollUtil.isNotEmpty(codes)) {
            ckQty = this.outboundMxService.getCkQty(codes, materialId, 0);
        }

        List<MaterialPojo> list = new ArrayList<>(produces.size());

        MaterialPojo pojo;
        for (ProduceEntity produce : produces) {
            pojo = new MaterialPojo();

            Long productId = produce.getProductId();
            ProductVo product = this.proFromRedis.getProduct(productId);
            if (ObjUtil.isNull(product)) {
                continue;
            }

            CountVo vo = vos.stream()
                    .filter(item -> item.getAId().equals(productId))
                    .findFirst()
                    .orElse(null);
            if (ObjUtil.isNull(vo)) {
                continue;
            }

            BigDecimal yield = product.getYield().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal qty = produce.getProductQty().multiply(vo.getQty()).divide(yield, 0, RoundingMode.UP);
            if (CollUtil.isNotEmpty(ckQty)) {
                CountVo ck = ckQty.stream()
                        .filter(item -> item.getAId().equals(produce.getId()))
                        .findFirst()
                        .orElse(null);

                if (ObjUtil.isNotNull(ck)) {
                    qty = qty.subtract(ck.getQty());
                }
            }

            if (qty.compareTo(BigDecimal.ZERO) < 1) {
                continue;
            }

            pojo.setSaleOrderCode(produce.getOrderNo());
            pojo.setPlanCode(produce.getCode());
            pojo.setProductCode(product.getCode());
            pojo.setProductTitle(product.getTitle());
            pojo.setReceiveQty(qty);
            list.add(pojo);
        }

        return new AsyncResult<>(list);
    }

}
