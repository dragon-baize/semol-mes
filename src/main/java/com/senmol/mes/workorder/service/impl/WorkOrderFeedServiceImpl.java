package com.senmol.mes.workorder.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.warehouse.entity.RetrievalMxEntity;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.workorder.entity.WorkOrderFeedEntity;
import com.senmol.mes.workorder.mapper.WorkOrderFeedMapper;
import com.senmol.mes.workorder.page.Retrospect;
import com.senmol.mes.workorder.service.WorkOrderFeedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 上料记录(WorkOrderFeed)表服务实现类
 *
 * @author makejava
 * @since 2023-10-23 10:44:08
 */
@Service("workOrderFeedService")
public class WorkOrderFeedServiceImpl extends ServiceImpl<WorkOrderFeedMapper, WorkOrderFeedEntity> implements WorkOrderFeedService {

    @Resource
    private RetrievalMxService retrievalMxService;
    @Resource
    private ProFromRedis proFromRedis;

    @Override
    public SaResult getByMxId(Long mxId, Long stationId, Long materialId) {
        return SaResult.data(this.baseMapper.getByMxId(mxId, stationId, materialId));
    }

    @Override
    public List<WorkOrderFeedEntity> getByMaterials(Long mxId, Long stationId, List<Long> materialIds) {
        return this.baseMapper.getByMaterials(mxId, stationId, materialIds);
    }

    @Override
    public SaResult retrospect(Retrospect retrospect) {
        if (ObjUtil.isNull(retrospect.getProductId())) {
            return SaResult.error("请选择产品");
        }

        List<Retrospect> list = this.baseMapper.retrospect(retrospect);
        if (CollUtil.isEmpty(list)) {
            return SaResult.data(null);
        }

        List<String> qrCodes = list.stream().map(Retrospect::getQrCode).collect(Collectors.toList());
        List<Retrospect> retrievals = this.baseMapper.getUseQty(qrCodes);

        Long mxId = 0L;
        BigDecimal total = BigDecimal.ZERO;
        int size = list.size() / 2;
        Map<Long, Retrospect> map = MapUtil.newHashMap(size);
        for (Retrospect item : list) {
            Retrospect orElse = retrievals.stream()
                    .filter(r -> r.getQrCode().equals(item.getQrCode()))
                    .findFirst()
                    .orElse(null);
            if (orElse != null) {
                item.setTaxPrice(orElse.getTaxPrice());
                item.setType(orElse.getType());
            } else {
                item.setTaxPrice(BigDecimal.ZERO);
                item.setType(2);
            }

            Long id = item.getId();
            if (!mxId.equals(id)) {
                total = total.add(item.getQty());
                mxId = id;
            }

            Long materialId = item.getMaterialId();
            Retrospect value = map.get(materialId);
            if (ObjUtil.isNull(value)) {
                this.setCodeAndTitle(item);
                item.setTotalPrice(item.getUsedQty().multiply(item.getTaxPrice()));
                map.put(materialId, item);
            } else {
                BigDecimal usedQty = item.getUsedQty();
                BigDecimal taxPrice = item.getTaxPrice();
                value.setUsedQty(value.getUsedQty().add(usedQty));
                value.setTotalPrice(value.getTotalPrice().add(usedQty.multiply(taxPrice)));
            }
        }

        return SaResult.data(MapUtil.builder().put("total", total).put("list", map.values()).build());
    }

    @Override
    public SaResult insertFeed(WorkOrderFeedEntity workOrderFeed) {
        RetrievalMxEntity retrievalMx = this.retrievalMxService.lambdaQuery().eq(RetrievalMxEntity::getQrCode,
                workOrderFeed.getQrCode()).one();

        BigDecimal qty = retrievalMx.getQty();
        BigDecimal usedQty = retrievalMx.getUsedQty();
        if (qty.compareTo(usedQty) <= 0) {
            return SaResult.error("上料剩余量不足");
        }

        workOrderFeed.setQty(qty.subtract(usedQty));
        this.save(workOrderFeed);
        return SaResult.data(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    private void setCodeAndTitle(Retrospect retrospect) {
        Integer type = retrospect.getType();
        if (ObjUtil.isNull(type)) {
            return;
        }

        if (type > 1) {
            MaterialVo material = this.proFromRedis.getMaterial(retrospect.getMaterialId());
            if (ObjUtil.isNotNull(material)) {
                retrospect.setMaterialCode(material.getCode());
                retrospect.setMaterialTitle(material.getTitle());
            }
        } else {
            ProductVo productVo = this.proFromRedis.getProduct(retrospect.getMaterialId());
            if (ObjUtil.isNotNull(productVo)) {
                retrospect.setMaterialCode(productVo.getCode());
                retrospect.setMaterialTitle(productVo.getTitle());
            }
        }
    }

}

