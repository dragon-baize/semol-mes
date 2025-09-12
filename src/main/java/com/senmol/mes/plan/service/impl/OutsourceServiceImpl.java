package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.common.utils.OutboundMaterial;
import com.senmol.mes.plan.entity.MrpOutsourceEntity;
import com.senmol.mes.plan.entity.OutsourceEntity;
import com.senmol.mes.plan.entity.SupplierGoods;
import com.senmol.mes.plan.mapper.OutsourceMapper;
import com.senmol.mes.plan.service.MrpOutsourceService;
import com.senmol.mes.plan.service.OutsourceService;
import com.senmol.mes.plan.service.SupplierGoodsService;
import com.senmol.mes.plan.until.PlanFromRedis;
import com.senmol.mes.plan.vo.SupplierVo;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 委外计划(Outsource)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 15:11:47
 */
@Service("outsourceService")
public class OutsourceServiceImpl extends ServiceImpl<OutsourceMapper, OutsourceEntity> implements OutsourceService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private MrpOutsourceService mrpOutsourceService;
    @Resource
    private StorageService storageService;
    @Resource
    private SupplierGoodsService supplierGoodsService;
    @Resource
    private PlanFromRedis planFromRedis;

    @Override
    public SaResult selectAll(Page<OutsourceEntity> page, OutsourceEntity outsource) {
        List<OutsourceEntity> list = this.baseMapper.selectAll(page, outsource);
        list.forEach(item -> {
            SupplierVo supplierVo = this.planFromRedis.getSupplier(item.getSupplierId());
            if (ObjUtil.isNotNull(supplierVo)) {
                item.setSupplierTitle(supplierVo.getName());
            }
        });

        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public SaResult inOutsource(List<Long> productIds, Long saleOrderId) {
        return SaResult.data(this.baseMapper.inOutsource(productIds, saleOrderId));
    }

    @Override
    public List<OutboundMaterial> getByCode(String code) {
        // 委外对应的清单物料类别总量
        List<OutboundMaterial> materials = this.baseMapper.getBaseQty(code);

        // 委外对应的清单物料库存
        Set<Long> goodsIds = materials.stream()
                .map(OutboundMaterial::getGoodsId)
                .collect(Collectors.toSet());
        List<CountVo> vos = this.storageService.getKcl(goodsIds, null);

        for (OutboundMaterial material : materials) {
            MaterialVo materialVo = this.proFromRedis.getMaterial(material.getGoodsId());
            if (ObjUtil.isNotNull(materialVo)) {
                material.setGoodsCode(materialVo.getCode());
                material.setGoodsTitle(materialVo.getTitle());
                material.setUnitTitle(this.sysFromRedis.getDictMx(materialVo.getUnitId()));
            }

            vos.stream()
                    .filter(item -> item.getAId().equals(material.getGoodsId()))
                    .findFirst()
                    .ifPresent(item -> material.setStorageQty(item.getQty()));
        }

        return materials;
    }

    @Override
    public OutsourceEntity getBySiCode(String siCode) {
        return this.baseMapper.getBySiCode(siCode);
    }

    @Override
    public List<OutsourceEntity> getByCpId(List<Long> productIds) {
        return this.baseMapper.getByCpId(productIds);
    }

    @Override
    public SaResult insertOutsource(OutsourceEntity outsource) {
        String checkIsExist = this.checkIsExist(outsource);
        if (checkIsExist != null) {
            return SaResult.error(checkIsExist);
        }

        SupplierGoods supplierGoods = this.supplierGoodsService.lambdaQuery()
                .eq(SupplierGoods::getType, 1)
                .eq(SupplierGoods::getGoodsId, outsource.getProductId())
                .last(CheckToolUtil.LIMIT)
                .one();
        if (ObjectUtil.isNotNull(supplierGoods)) {
            outsource.setSupplierId(supplierGoods.getPid());
            outsource.setPrice(supplierGoods.getPrice());
            outsource.setTaxRate(supplierGoods.getTaxRate());
            outsource.setTaxPrice(supplierGoods.getTaxPrice());
        }

        Date date = new Date();
        Long count = this.lambdaQuery().between(OutsourceEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        outsource.setCode("WW" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + (101 + count * 3));
        this.save(outsource);

        // 保存mrp-produce数据
        if (outsource.getMrp() == 0) {
            MrpOutsourceEntity mrpOutsource = new MrpOutsourceEntity(
                    outsource.getMrpId(),
                    outsource.getId()
            );

            this.mrpOutsourceService.save(mrpOutsource);
        }

        return SaResult.data(outsource);
    }

    @Override
    public SaResult insertOutsources(List<OutsourceEntity> outsources) {
        // 删除数量为0的数据
        outsources.removeIf(item -> item.getQty().compareTo(BigDecimal.ZERO) < 1);

        String checkIsExist = this.checkIsExist(outsources);
        if (StrUtil.isNotBlank(checkIsExist)) {
            return SaResult.error("委外计划已创建：" + checkIsExist);
        }

        List<SupplierGoods> supplierGoods = this.supplierGoodsService.lambdaQuery()
                .eq(SupplierGoods::getType, 1)
                .list();

        Date date = new Date();
        Long count = this.lambdaQuery().between(OutsourceEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
        String format = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        for (int i = 0, j = outsources.size(); i < j; i++) {
            OutsourceEntity outsource = outsources.get(i);
            outsource.setCode("WW" + format + (101 + (count + i) * 3));

            supplierGoods.stream()
                    .filter(item -> item.getGoodsId().equals(outsource.getProductId()))
                    .findFirst()
                    .ifPresent(p -> {
                        outsource.setSupplierId(p.getPid());
                        outsource.setPrice(p.getPrice());
                        outsource.setTaxRate(p.getTaxRate());
                        outsource.setTaxPrice(p.getTaxPrice());
                    });
        }

        this.saveBatch(outsources);

        List<MrpOutsourceEntity> list = new ArrayList<>(outsources.size());
        for (OutsourceEntity outsource : outsources) {
            MrpOutsourceEntity mrpOutsource = new MrpOutsourceEntity();
            mrpOutsource.setMrpId(outsource.getMrpId());
            mrpOutsource.setOutsourceId(outsource.getId());
            list.add(mrpOutsource);
        }

        this.mrpOutsourceService.saveBatch(list);
        return SaResult.data(outsources);
    }

    @Override
    public SaResult updateOutsource(OutsourceEntity outsource) {
        String checkIsExist = this.checkIsExist(outsource);
        if (checkIsExist != null) {
            return SaResult.error(checkIsExist);
        }

        this.updateById(outsource);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteOutsource(Long id) {
        OutsourceEntity outsource = this.getById(id);
        if (outsource.getMrp() == 0) {
            return SaResult.error("MRP生成的数据不允许在此处删除");
        }

        // TODO 创建出库单的能删除不，状态变更的删除不

        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    /**
     * 校验订单编号是否重复
     */
    private String checkIsExist(OutsourceEntity outsource) {
        LambdaQueryWrapper<OutsourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutsourceEntity::getOrderNo, outsource.getOrderNo());
        wrapper.eq(OutsourceEntity::getProductId, outsource.getProductId());

        Long id = outsource.getId();
        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(OutsourceEntity::getId, id);
        }

        wrapper.last(CheckToolUtil.LIMIT);
        long count = this.count(wrapper);

        if (count > 0L) {
            return "委外计划已创建";
        }

        BomVo bom = this.proFromRedis.getBom(outsource.getProductId());
        if (ObjectUtil.isNull(bom) || bom.getStatus() != 0) {
            return "产品物料清单不存在或未审批通过";
        }

        return null;
    }

    /**
     * 批量校验编号是否重复
     */
    private String checkIsExist(List<OutsourceEntity> outsources) {
        List<OutsourceEntity> list = this.list();

        long count;
        for (OutsourceEntity entity : list) {
            count = outsources.stream().filter(item ->
                    item.getOrderNo().equals(entity.getOrderNo()) &&
                            item.getProductId().equals(entity.getProductId())
            ).count();

            if (count > 0) {
                return entity.getTitle();
            }
        }

        return null;
    }

}

