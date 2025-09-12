package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.plan.entity.MrpRequisitionEntity;
import com.senmol.mes.plan.entity.RequisitionEntity;
import com.senmol.mes.plan.mapper.RequisitionMapper;
import com.senmol.mes.plan.page.RequisitionPage;
import com.senmol.mes.plan.service.MrpRequisitionService;
import com.senmol.mes.plan.service.RequisitionService;
import com.senmol.mes.plan.vo.RequisitionVo;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 请购单(Requisition)表服务实现类
 *
 * @author makejava
 * @since 2023-03-13 15:57:22
 */
@Service("requisitionService")
public class RequisitionServiceImpl extends ServiceImpl<RequisitionMapper, RequisitionEntity> implements RequisitionService {

    @Resource
    private MrpRequisitionService mrpRequisitionService;
    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public SaResult selectAll(RequisitionPage page, RequisitionEntity requisition, String keyword) {
        CompletableFuture<List<RequisitionVo>> selectAll =
                CompletableFuture.supplyAsync(() -> this.baseMapper.selectAll(page, requisition, keyword),
                        this.executor).exceptionally(e -> {
                            e.printStackTrace();
                            throw new BusinessException("请购单列表查询失败，请重试");
                        });

        if (page.getSize() == -1) {
            page.setRecords(selectAll.join());
            return SaResult.data(page);
        }

        CompletableFuture<BigDecimal> selectTotal =
                CompletableFuture.supplyAsync(() -> this.baseMapper.selectTotal(page.getStartTime(),
                                page.getEndTime(), requisition, keyword), this.executor)
                        .exceptionally(e -> {
                            e.printStackTrace();
                            throw new BusinessException("合计统计失败，请重试");
                        });

        RequisitionPage join = selectAll.thenCombine(selectTotal, (records, adviceQty) -> {
            page.setRecords(records);
            page.setTotalAdviceQty(adviceQty);
            return page;
        }).join();

        return SaResult.data(join);
    }

    @Override
    public List<RequisitionEntity> getUnbound() {
        return this.baseMapper.getUnbound();
    }

    @Override
    public SaResult saveRequisition(RequisitionEntity requisition) {
        Date date = new Date();
        Long count = this.lambdaQuery().between(RequisitionEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        requisition.setCode("QGD" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + count * 3));
        this.save(requisition);

        if (requisition.getMrp() == 0) {
            MrpRequisitionEntity entity = new MrpRequisitionEntity(
                    requisition.getMrpId(),
                    requisition.getId()
            );

            this.mrpRequisitionService.save(entity);
        }

        return SaResult.data(requisition);
    }

    @Override
    public SaResult saveRequisitions(List<RequisitionEntity> requisitions) {
        // 删除建议采购数量为0的请购单
        requisitions.removeIf(item -> item.getAdviceQty().compareTo(BigDecimal.ZERO) < 1);

        if (CollUtil.isEmpty(requisitions)) {
            return SaResult.ok();
        }

        // 存在的请购单无需创建
        long count = this.mrpRequisitionService.lambdaQuery()
                .eq(MrpRequisitionEntity::getMrpId, requisitions.get(0).getMrpId())
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("MRP对应的请购单已创建");
        }

        Date date = new Date();
        Long total = this.lambdaQuery().between(RequisitionEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        String format = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);

        // 同种物料数据合并
        Map<Long, RequisitionEntity> map = MapUtil.newHashMap(requisitions.size());
        for (int i = 0, j = requisitions.size(); i < j; i++) {
            RequisitionEntity requisition = requisitions.get(i);
            Long materialId = requisition.getMaterialId();
            RequisitionEntity entity = map.get(materialId);
            if (ObjUtil.isNull(entity)) {
                requisition.setCode("QGD" + format + (101 + (total + i) * 3));

                map.put(materialId, requisition);
            } else {
                BigDecimal add = entity.getAdviceQty().add(requisition.getAdviceQty());
                entity.setAdviceQty(add);

                map.put(materialId, entity);
            }
        }

        Collection<RequisitionEntity> values = map.values();
        this.saveBatch(values);

        List<MrpRequisitionEntity> list = new ArrayList<>(values.size());
        for (RequisitionEntity value : values) {
            MrpRequisitionEntity entity = new MrpRequisitionEntity(
                    value.getMrpId(),
                    value.getId()
            );

            list.add(entity);
        }

        this.mrpRequisitionService.saveBatch(list);

        return SaResult.data(values);
    }

    @Override
    public SaResult updateRequisition(RequisitionEntity requisition) {
        this.updateById(requisition);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult removeRequisition(Long id) {
        RequisitionEntity byId = this.getById(id);
        if (byId.getMrp() == 0) {
            return SaResult.error("MRP生成的数据不允许在此处删除");
        }

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

