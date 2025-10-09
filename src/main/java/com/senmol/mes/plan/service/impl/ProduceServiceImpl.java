package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.plan.entity.MrpProduceEntity;
import com.senmol.mes.plan.entity.ProduceEntity;
import com.senmol.mes.plan.mapper.ProduceMapper;
import com.senmol.mes.plan.service.MrpProduceService;
import com.senmol.mes.plan.service.ProduceService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.plan.vo.ProduceVo;
import com.senmol.mes.plan.vo.ProductQty;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.produce.vo.LineVo;
import com.senmol.mes.workorder.entity.WorkOrderMxEntity;
import com.senmol.mes.workorder.service.WorkOrderMxService;
import com.senmol.mes.workorder.vo.MaterialPojo;
import com.senmol.mes.workorder.vo.ProductLineInfo;
import com.senmol.mes.workorder.vo.WorkOrderPojo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * 生产计划(Produce)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Service("produceService")
public class ProduceServiceImpl extends ServiceImpl<ProduceMapper, ProduceEntity> implements ProduceService {

    @Resource
    private PlanFromRedis planFromRedis;
    @Resource
    private WorkOrderMxService workOrderMxService;
    @Resource
    private MrpProduceService mrpProduceService;
    @Resource
    private RedisService redisService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private ProductLineService productLineService;

    @Override
    public SaResult selectAll(Page<ProduceVo> page, ProduceEntity produce) {
        List<ProduceVo> list = this.baseMapper.selectAll(page, produce);
        list.forEach(item -> {
            item.setCustomTitle(this.planFromRedis.getCustom(item.getCustomId()));

            LineVo line = this.proFromRedis.getLine(item.getProductLineId());
            if (ObjUtil.isNotNull(line)) {
                item.setProductLineCode(line.getCode());
                item.setProductLineTitle(line.getTitle());
            }
        });

        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public List<ProduceVo> byProductLineId(Long productLineId) {
        return this.baseMapper.byProductLineId(productLineId);
    }

    @Override
    public ProduceVo selectOne(Long id) {
        ProduceVo vo = this.baseMapper.getOne(id);
        LineVo line = this.proFromRedis.getLine(vo.getProductLineId());
        if (ObjUtil.isNotNull(line)) {
            vo.setProductLineCode(line.getCode());
            vo.setProductLineTitle(line.getTitle());
        }

        vo.setCustomTitle(this.planFromRedis.getCustom(vo.getCustomId()));
        return vo;
    }

    @Override
    public List<ProductQty> getSumQty(List<Long> productIds, Long saleOrderId) {
        return this.baseMapper.getSumQty(productIds, saleOrderId);
    }

    @Override
    public List<MaterialPojo> getPlanInfo(Set<Long> ids) {
        return this.baseMapper.getPlanInfo(ids);
    }

    @Override
    public SaResult inProcess(List<Long> productIds) {
        return SaResult.data(this.baseMapper.getProductionQty(productIds));
    }

    @Override
    public List<ProductLineInfo> statOrder(Long productLineId) {
        return this.baseMapper.statOrder(productLineId);
    }

    @Override
    public WorkOrderPojo getByMxCode(String mxCode) {
        return this.baseMapper.getByMxCode(mxCode);
    }

    @Override
    public WorkOrderPojo getByMxId(Long mxId) {
        return this.baseMapper.getByMxId(mxId);
    }

    @Override
    public SaResult workbench() {
        Map<String, BigDecimal> map = new HashMap<>(6);
        // 计划数量
        map.put("all", this.baseMapper.getAllTotal());
        // 统计实际数量
        map.put("act", this.productLineService.getAllTotal());

        // 入库数量
        BigDecimal storageQty = this.workOrderMxService.getStorageQty();
        if (storageQty.compareTo(BigDecimal.ZERO) > 0) {
            // 工单总数
            BigDecimal sumQty = this.workOrderMxService.getSumQty();

            map.put("badMode", storageQty);
            map.put("yield", storageQty.divide(sumQty, 4, RoundingMode.HALF_UP));
        } else {
            map.put("badMode", BigDecimal.ZERO);
            map.put("yield", BigDecimal.ZERO);
        }

        return SaResult.data(map);
    }

    @Override
    public SaResult insertProduce(ProduceEntity produce) {
        String checkProduct = this.checkProduct(produce.getProductId());
        if (checkProduct != null) {
            return SaResult.error(checkProduct);
        }

        this.save(produce);

        // 保存mrp-produce数据
        if (produce.getMrp() == 0) {
            MrpProduceEntity mrpProduce = new MrpProduceEntity(
                    produce.getMrpId(),
                    produce.getId()
            );

            this.mrpProduceService.save(mrpProduce);
        }

        return SaResult.data(produce);
    }

    @Override
    public SaResult insertProduces(List<ProduceEntity> produces) {
        // 删除生产数量为0的数据
        produces.removeIf(item -> item.getProductQty().compareTo(BigDecimal.ZERO) < 1);

        String date = LocalDate.now().toString();
        int count = this.baseMapper.getTodayCount(date);
        String format = date.replace("-", "");
        for (int i = 0, j = produces.size(); i < j; i++) {
            ProduceEntity produce = produces.get(i);
            produce.setCode("SC" + format + (101 + (count + i) * 3));
        }

        this.saveBatch(produces);

        List<MrpProduceEntity> mrpProduces = new ArrayList<>(produces.size());
        for (ProduceEntity produce : produces) {
            MrpProduceEntity mrpProduce = new MrpProduceEntity();
            mrpProduce.setMrpId(produce.getMrpId());
            mrpProduce.setProduceId(produce.getId());
            mrpProduces.add(mrpProduce);
        }

        this.mrpProduceService.saveBatch(mrpProduces);
        return SaResult.data(produces);
    }

    @Override
    public SaResult updateProduce(ProduceEntity produce) {
        String checkProduct = this.checkProduct(produce.getProductId());
        if (checkProduct != null) {
            return SaResult.error(checkProduct);
        }

        this.updateById(produce);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public SaResult closeOrder(ProduceEntity produce) {
        Long id = produce.getId();

        Long count = this.workOrderMxService.lambdaQuery().eq(WorkOrderMxEntity::getPid, id)
                .eq(WorkOrderMxEntity::getFinish, 0).ne(WorkOrderMxEntity::getStatus, 2).count();
        if (count > 0L) {
            return SaResult.error("存在未完成的工单，请先终止工单");
        }

        // 设置计划的实际完成时间
        produce.setRealityFinishTime(LocalDate.now());
        produce.setStatus(2);
        boolean b = this.updateById(produce);
        if (b) {
            this.workOrderMxService.lambdaUpdate().set(WorkOrderMxEntity::getFinish, 2)
                    .ne(WorkOrderMxEntity::getStatus, 2).eq(WorkOrderMxEntity::getPid, id).update();
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteProduce(Long id) {
        ProduceEntity produce = this.getById(id);
        if (produce.getMrp() == 0) {
            return SaResult.error("MRP生成的数据不允许在此处删除");
        }

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private String checkProduct(Long productId) {
        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_BOM.getKey() + productId);
        BomVo bomVo = (BomVo) object;
        if (ObjectUtil.isNull(bomVo) || bomVo.getStatus() != 0) {
            return "产品清单不存在或清单未审批通过";
        }

        return null;
    }

}

