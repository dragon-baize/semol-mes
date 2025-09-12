package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CommonPojo;
import com.senmol.mes.plan.entity.*;
import com.senmol.mes.plan.mapper.MrpMapper;
import com.senmol.mes.plan.service.*;
import com.senmol.mes.plan.vo.MaterialVo;
import com.senmol.mes.plan.vo.MrpInfo;
import com.senmol.mes.plan.vo.ProductQty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MRP(Mrp)表服务实现类
 *
 * @author makejava
 * @since 2023-07-15 11:18:11
 */
@Service("mrpService")
public class MrpServiceImpl extends ServiceImpl<MrpMapper, MrpEntity> implements MrpService {

    @Resource
    private MrpProduceService mrpProduceService;
    @Resource
    private MrpRequisitionService mrpRequisitionService;
    @Resource
    private SaleOrderProductService saleOrderProductService;
    @Resource
    private MrpOutsourceService mrpOutsourceService;
    @Resource
    private MrpProductService mrpProductService;
    @Resource
    private MrpMaterialService mrpMaterialService;
    @Resource
    private SaleOrderService saleOrderService;
    @Resource
    private ProduceService produceService;

    @Override
    public MrpInfo selectOne(Long id) {
        MrpEntity mrp = this.getById(id);
        MrpInfo info = Convert.convert(MrpInfo.class, mrp);

        List<MrpProduct> products = this.mrpProductService.lambdaQuery()
                .eq(MrpProduct::getMrpId, id)
                .list();

        List<MrpMaterial> materials = this.mrpMaterialService.lambdaQuery()
                .eq(MrpMaterial::getMrpId, id)
                .list();

        info.setProducts(products);
        info.setMaterials(materials);
        return info;
    }

    @Override
    public Page<MrpEntity> selectAll(Page<MrpEntity> page, MrpEntity mrp) {
        List<MrpEntity> list = this.baseMapper.selectAll(page, mrp);
        page.setRecords(list);
        return page;
    }

    @Override
    public SaResult insertMrp(MrpInfo info) {
        long count = this.lambdaQuery()
                .eq(MrpEntity::getSaleOrderId, info.getSaleOrderId())
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.ok("已计算过的MRP数据");
        }

        MrpEntity mrp = Convert.convert(MrpEntity.class, info);
        this.save(mrp);

        // 产品信息存储
        List<MrpProduct> products = info.getProducts();
        products.forEach(item -> item.setMrpId(mrp.getId()));
        this.mrpProductService.saveBatch(products);

        // 物料信息存储
        List<MrpMaterial> materials = info.getMaterials();
        materials.forEach(item -> item.setMrpId(mrp.getId()));
        this.mrpMaterialService.saveBatch(materials);

        // 修改销售订单为已计算
        this.saleOrderService.lambdaUpdate()
                .set(SaleOrderEntity::getStatus, 1)
                .eq(SaleOrderEntity::getId, mrp.getSaleOrderId())
                .update();
        return SaResult.ok();
    }

    @Override
    public SaResult unfinishedOrder(List<Long> productIds, Long saleOrderId) {
        // 销售订单的产品量
        List<ProductQty> sumQty = this.saleOrderProductService.getSumQty(productIds, saleOrderId);

        List<ProductQty> list = new ArrayList<>();
        for (ProductQty productQty : sumQty) {
            ProductQty qty = list.stream()
                    .filter(item -> item.getGoodsId().equals(productQty.getGoodsId()))
                    .findFirst()
                    .orElse(null);
            if (ObjectUtil.isNotNull(qty)) {
                qty.setQty(qty.getQty().add(productQty.getQty()));
            } else {
                list.add(productQty);
            }
        }

        return SaResult.data(list);
    }

    @Override
    public SaResult materialQty(Long productId) {
        List<MaterialVo> vos = this.baseMapper.materialQty(productId);
        Map<Long, BigDecimal> map = vos.stream()
                .filter(item -> item.getType().equals(1))
                .collect(Collectors.toMap(MaterialVo::getMaterialId, MaterialVo::getQty));

        Set<Map.Entry<Long, BigDecimal>> set = map.entrySet();
        for (Map.Entry<Long, BigDecimal> entry : set) {
            List<MaterialVo> tmp = this.baseMapper.materialQty(entry.getKey());
            tmp.forEach(item -> item.setQty(item.getQty().multiply(entry.getValue())));
            vos.addAll(tmp);
        }

        List<MaterialVo> list = new ArrayList<>();
        for (MaterialVo vo : vos) {
            MaterialVo materialVo = list.stream()
                    .filter(item -> item.getMaterialId().equals(vo.getMaterialId()))
                    .findFirst()
                    .orElse(null);

            if (materialVo == null) {
                list.add(vo);
            } else {
                materialVo.setQty(materialVo.getQty().add(vo.getQty()));
            }
        }

        return SaResult.data(list);
    }

    @Override
    public SaResult deleteMrp(Long id) {
        Integer produceCount = this.baseMapper.getProduceCount(id);
        if (produceCount > 0) {
            return SaResult.error("存在已生产的生产计划");
        }

        List<CommonPojo> list = this.baseMapper.getWorkOrderCount(id);
        long count = list.stream()
                .filter(item -> Integer.parseInt(item.getCode()) == 1)
                .count();
        if (count > 0L) {
            return SaResult.error("存在已打印的工单");
        }

        Integer requisitionCount = this.baseMapper.getRequisitionCount(id);
        if (requisitionCount > 0) {
            return SaResult.error("存在已生成采购订单的请购单");
        }

        Integer outsourceCount = this.baseMapper.getOutsourceCount(id);
        if (outsourceCount > 0) {
            return SaResult.error("存在已创建出库单的委外计划");
        }

        List<Long> ids = list.stream().map(CommonPojo::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)) {
            this.baseMapper.delWorkOrderMaterial(ids);
            this.baseMapper.delWorkOrderMxMaterial(ids);
            this.baseMapper.delWorkOrderMx(ids);
        }

        this.removeById(id);
        this.baseMapper.delProduces(id);
        this.baseMapper.delRequisitions(id);
        this.baseMapper.delOutsource(id);
        this.mrpProduceService.lambdaUpdate().eq(MrpProduceEntity::getMrpId, id).remove();
        this.mrpRequisitionService.lambdaUpdate().eq(MrpRequisitionEntity::getMrpId, id).remove();
        this.mrpOutsourceService.lambdaUpdate().eq(MrpOutsourceEntity::getMrpId, id).remove();
        this.mrpMaterialService.lambdaUpdate().eq(MrpMaterial::getMrpId, id).remove();
        this.mrpProductService.lambdaUpdate().eq(MrpProduct::getMrpId, id).remove();
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    @Override
    public SaResult unSaleOrder() {
        List<MaterialVo> list = this.baseMapper.unSaleOrder();

        List<MaterialVo> halfProducts = list.stream()
                .filter(item -> Objects.nonNull(item) && item.getType().equals(1))
                .collect(Collectors.toList());

        List<ProduceEntity> produces = new ArrayList<>(halfProducts.size());
        if (CollUtil.isNotEmpty(halfProducts)) {
            Map<Long, BigDecimal> map = halfProducts.stream().collect(Collectors.toMap(MaterialVo::getMaterialId,
                    MaterialVo::getQty));
            List<MaterialVo> materialVos = this.baseMapper.getByProductIds(map);
            list.addAll(materialVos);

            produces = this.produceService.lambdaQuery()
                    .in(ProduceEntity::getProductId, map.keySet())
                    .ne(ProduceEntity::getStatus, 2)
                    .list();
        }

        List<MaterialVo> vos = new ArrayList<>();
        for (MaterialVo vo : list) {
            if (vo == null) {
                continue;
            }

            MaterialVo materialVo = vos.stream()
                    .filter(item -> item.getMaterialId().equals(vo.getMaterialId()))
                    .findFirst()
                    .orElse(null);

            if (vo.getType() == 1) {
                produces.stream()
                        .filter(item -> item.getProductId().equals(vo.getMaterialId()))
                        .findFirst()
                        .ifPresent(item -> vo.setHpQty(item.getProductQty()));
            }

            if (ObjectUtil.isNull(materialVo)) {
                vos.add(vo);
            } else {
                materialVo.setQty(materialVo.getQty().add(vo.getQty()));
            }
        }

        return SaResult.data(vos);
    }

    @Override
    public SaResult unWorkOrder() {
        List<MaterialVo> list = this.baseMapper.unWorkOrder();

        List<MaterialVo> vos = new ArrayList<>();
        for (MaterialVo vo : list) {
            MaterialVo materialVo = vos.stream()
                    .filter(item -> Objects.nonNull(item) && item.getMaterialId().equals(vo.getMaterialId()))
                    .findFirst()
                    .orElse(null);

            if (ObjectUtil.isNull(materialVo)) {
                vos.add(vo);
            } else {
                materialVo.setQty(materialVo.getQty().add(vo.getQty()));
            }
        }

        return SaResult.data(vos);
    }

}

