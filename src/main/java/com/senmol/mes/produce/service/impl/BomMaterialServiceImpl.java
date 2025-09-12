package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.plan.until.PlanAsyncUtil;
import com.senmol.mes.produce.entity.BomMaterialEntity;
import com.senmol.mes.produce.mapper.BomMaterialMapper;
import com.senmol.mes.produce.service.BomMaterialService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.produce.vo.BomVo;
import com.senmol.mes.produce.vo.MaterialInfo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 清单-物料(BomMaterial)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("bomMaterialService")
public class BomMaterialServiceImpl extends ServiceImpl<BomMaterialMapper, BomMaterialEntity> implements BomMaterialService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private PlanAsyncUtil planAsyncUtil;

    @Override
    public SaResult getByProductId(Long productId) {
        ProductVo product = this.proFromRedis.getProduct(productId);
        if (ObjUtil.isNull(product)) {
            return SaResult.error("产品数据不存在");
        }

        BomVo bomVo = this.proFromRedis.getBom(productId);
        if (ObjUtil.isNull(bomVo) || bomVo.getStatus() != 0) {
            return SaResult.error("产品未绑定物料清单");
        }

        // 获取产品清单物料
        List<MaterialInfo> infos = this.baseMapper.getByProductId(productId);
        if (CollUtil.isEmpty(infos)) {
            return SaResult.error("产品绑定物料清单没有物料");
        }

        Set<Long> materialIds = infos.stream()
                .map(MaterialInfo::getId)
                .collect(Collectors.toSet());

        Future<Map<Long, BigDecimal>> wwJhInfo = this.planAsyncUtil.wwJhInfo(materialIds);
        Future<List<Long>> scJhInfo = this.planAsyncUtil.scJhInfo(infos, materialIds);

        Map<Long, BigDecimal> map;
        List<Long> planIds;
        try {
            map = wwJhInfo.get();
            planIds = scJhInfo.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new BusinessException("产品计划信息计算出错，请重试！");
        }

        Future<List<MaterialInfo>> fProduct = this.planAsyncUtil.dealInfo(
                new ArrayList<>(infos),
                planIds,
                new HashMap<>(0),
                true);
        Future<List<MaterialInfo>> fMaterials = this.planAsyncUtil.dealInfo(
                new ArrayList<>(infos),
                planIds,
                map,
                false);

        try {
            List<MaterialInfo> products = fProduct.get();
            List<MaterialInfo> materials = fMaterials.get();

            products.addAll(materials);
            return SaResult.data(products);
        } catch (InterruptedException | ExecutionException e) {
            throw new BusinessException("产品、物料信息计算出错，请重试！");
        }
    }

    @Override
    public SaResult getBomMaterial(Long productId) {
        BomVo bomVo = this.proFromRedis.getBom(productId);
        if (ObjUtil.isNull(bomVo)) {
            return SaResult.error("产品未绑定物料清单");
        }

        List<BomMaterialVo> materialVos = bomVo.getMaterialVos();

        List<BomMaterialEntity> list = new ArrayList<>(materialVos.size());
        for (BomMaterialVo bomMaterialVo : materialVos) {
            BomMaterialEntity bomMaterial = Convert.convert(BomMaterialEntity.class, bomMaterialVo);
            // 物料
            BomServiceImpl.setMaterialValue(bomMaterial, this.proFromRedis, this.sysFromRedis);
            list.add(bomMaterial);
        }

        return SaResult.data(list);
    }

}

