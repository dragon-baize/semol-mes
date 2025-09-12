package com.senmol.mes.plan.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.plan.entity.*;
import com.senmol.mes.plan.mapper.OutboundMapper;
import com.senmol.mes.plan.service.*;
import com.senmol.mes.plan.vo.OutboundInfo;
import com.senmol.mes.plan.vo.OutboundMxVo;
import com.senmol.mes.plan.vo.OutboundVo;
import com.senmol.mes.plan.vo.ProductQty;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.service.RetrievalMxService;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库单(Outbound)表服务实现类
 *
 * @author makejava
 * @since 2023-03-13 10:21:09
 */
@Service("outboundService")
public class OutboundServiceImpl extends ServiceImpl<OutboundMapper, OutboundEntity> implements OutboundService {

    @Resource
    private SaleOrderProductService saleOrderProductService;
    @Resource
    private SaleOrderService saleOrderService;
    @Resource
    private OutboundMxService outboundMxService;
    @Resource
    private OutsourceService outsourceService;
    @Resource
    private StorageService storageService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private RetrievalMxService retrievalMxService;

    @Override
    public SaResult selectAll(Page<OutboundVo> page, OutboundEntity outbound) {
        List<OutboundVo> list = this.baseMapper.selectAll(page, outbound);
        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public SaResult selectOne(Long id) {
        OutboundEntity outbound = this.getById(id);
        OutboundVo outboundVo = Convert.convert(OutboundVo.class, outbound);
        Integer type = outbound.getType();
        if (type > 2 && type != 10) {
            return this.scrap(type, outboundVo, 0);
        } else {
            return this.nonScrap(outboundVo, 0);
        }
    }

    @Override
    public SaResult mx(Long id) {
        // 查询出库单信息
        OutboundEntity outbound = this.getById(id);

        // 查询出库明细表信息
        List<OutboundMxEntity> mxs = this.outboundMxService.lambdaQuery()
                .eq(OutboundMxEntity::getOutboundId, id)
                .list();

        // 查询出库表明细信息
        List<OutboundMxVo> retrievals = this.retrievalMxService.getByOutBoundCode(outbound.getCode());

        OutboundMxVo outboundMxVo;
        List<OutboundMxVo> list = new ArrayList<>(mxs.size() * 2);
        for (OutboundMxEntity mx : mxs) {
            Long goodsId = mx.getGoodsId();
            ProductVo product = this.proFromRedis.getProduct(goodsId);
            String code = ObjUtil.isNull(product) ? "" : product.getCode();
            String title = ObjUtil.isNull(product) ? "" : product.getTitle();
            String unitTitle = ObjUtil.isNull(product) ? "" : this.sysFromRedis.getDictMx(product.getUnitId());

            List<OutboundMxVo> vos =
                    retrievals.stream().filter(item -> item.getGoodsId().equals(goodsId)).collect(Collectors.toList());
            if (CollUtil.isEmpty(vos)) {
                outboundMxVo = new OutboundMxVo();
                outboundMxVo.setGoodsId(goodsId);
                outboundMxVo.setGoodsCode(code);
                outboundMxVo.setGoodsTitle(title);
                outboundMxVo.setUnitTitle(unitTitle);
                outboundMxVo.setQty(mx.getQty());
                outboundMxVo.setPrice(mx.getPrice());
                outboundMxVo.setAmount(mx.getQty().multiply(mx.getPrice()).setScale(2, RoundingMode.HALF_UP));
                outboundMxVo.setTaxRate(mx.getTaxRate());
                outboundMxVo.setTaxPrice(mx.getTaxPrice());
                outboundMxVo.setTaxAmount(mx.getQty().multiply(mx.getTaxPrice()).setScale(2, RoundingMode.HALF_UP));
                list.add(outboundMxVo);
            } else {
                for (OutboundMxVo mxVo : vos) {
                    outboundMxVo = new OutboundMxVo();
                    outboundMxVo.setBatchNo(mxVo.getBatchNo());
                    outboundMxVo.setGoodsId(goodsId);
                    outboundMxVo.setGoodsCode(code);
                    outboundMxVo.setGoodsTitle(title);
                    outboundMxVo.setUnitTitle(unitTitle);
                    outboundMxVo.setQty(mxVo.getQty());
                    outboundMxVo.setPrice(mx.getPrice());
                    outboundMxVo.setAmount(mxVo.getQty().multiply(mx.getPrice()).setScale(2, RoundingMode.HALF_UP));
                    outboundMxVo.setTaxRate(mx.getTaxRate());
                    outboundMxVo.setTaxPrice(mx.getTaxPrice());
                    outboundMxVo.setTaxAmount(mxVo.getQty().multiply(mx.getTaxPrice()).setScale(2,
                            RoundingMode.HALF_UP));
                    list.add(outboundMxVo);
                }
            }
        }

        return SaResult.data(list);
    }

    @Override
    public SaResult getByCode(String code) {
        OutboundEntity outbound = this.lambdaQuery().eq(OutboundEntity::getCode, code).one();
        if (ObjectUtil.isNull(outbound)) {
            return SaResult.error("出库单数据不存在");
        }

        OutboundVo outboundVo = Convert.convert(OutboundVo.class, outbound);
        Integer type = outbound.getType();
        if (type > 2) {
            return this.scrap(type, outboundVo, 1);
        } else {
            return this.nonScrap(outboundVo, 1);
        }
    }

    @Override
    public List<ProductQty> getSumQty(List<Long> productIds) {
        return this.baseMapper.getSumQty(productIds);
    }

    @Override
    public List<CountVo> getOutBoundQty(Set<String> codes, Set<Long> ids) {
        return this.baseMapper.getOutBoundQty(codes, ids);
    }

    @Override
    public List<CountVo> getObQty(Set<String> codes, Long materialId) {
        return this.baseMapper.getObQty(codes, materialId);
    }

    @Override
    public List<CountVo> getOutsourceQty(Set<Long> ids) {
        return this.baseMapper.getOutsourceQty(ids);
    }

    @Override
    public List<CountVo> getOsQty(Long materialId) {
        return this.baseMapper.getOsQty(materialId);
    }

    @Override
    public SaResult insertOutbound(OutboundVo outboundVo) {
        Integer type = outboundVo.getType();
        String orderNo = outboundVo.getOrderNo();
        if (type == 1) {
            Long total = this.outsourceService.lambdaQuery().eq(OutsourceEntity::getCode, orderNo).count();
            if (total > 0L) {
                return SaResult.error("【" + orderNo + "】已创建出库单");
            }
        }

        Date date = new Date();
        DateTime beginOfDay = DateUtil.beginOfDay(date);
        DateTime endOfDay = DateUtil.endOfDay(date);
        String format = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);

        Long count = this.lambdaQuery().between(OutboundEntity::getCreateTime, beginOfDay, endOfDay).count();
        outboundVo.setCode("CKD" + format + (101 + count * 3));
        OutboundEntity outbound = Convert.convert(OutboundEntity.class, outboundVo);
        boolean b = this.save(outbound);
        if (b) {
            // 委外出库只能创建一次出库单
            if (outbound.getType() == 1) {
                this.outsourceService.lambdaUpdate().set(OutsourceEntity::getIsCreate, 1).eq(OutsourceEntity::getCode, orderNo).update();
            }

            List<OutboundMxEntity> mxs = outboundVo.getMxs();
            mxs.forEach(item -> item.setOutboundId(outbound.getId()));
            this.outboundMxService.saveBatch(mxs);
        }

        return SaResult.data(outbound);
    }

    @Override
    public boolean saveOutbound(OutboundEntity outbound) {
        return this.baseMapper.saveOutbound(outbound) > 0;
    }

    @Override
    public SaResult insertInvoice(OutboundInfo outbound) {
        // TODO 销售发货单发货单号
        OutboundEntity entity = Convert.convert(OutboundEntity.class, outbound);

        Date date = new Date();
        Long count = this.lambdaQuery().between(OutboundEntity::getCreateTime, DateUtil.beginOfDay(date),
                DateUtil.endOfDay(date)).count();
        entity.setCode("XS-" + DateUtil.format(new Date(), "yyyy-MMdd-") + ++count);
        entity.setType(10);
        entity.setOrderNo(entity.getOrderNo());
        this.save(entity);

        List<SaleOrderProductEntity> products = outbound.getProducts().stream()
                .filter(item -> item.getRealityQty().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        this.saleOrderProductService.modifyBatchById(products, outbound.getOrderNo());

        List<OutboundMxEntity> list = new ArrayList<>(products.size());
        Long id = entity.getId();
        OutboundMxEntity mx;
        for (SaleOrderProductEntity product : products) {
            mx = new OutboundMxEntity();
            mx.setGoodsId(product.getProductId());
            mx.setType(0);
            mx.setQty(product.getRealityQty());
            mx.setPrice(product.getPrice());
            mx.setTaxRate(product.getTaxRate());
            mx.setTaxPrice(product.getTaxPrice());
            mx.setOutboundId(id);
            list.add(mx);
        }

        int sign = 2;
        List<String> countSign = this.saleOrderProductService.countSign(outbound.getOrderNo());
        if (countSign.contains("a")) {
            sign = 1;
        }

        this.saleOrderService.lambdaUpdate()
                .set(SaleOrderEntity::getSign, sign)
                .set(SaleOrderEntity::getUpdateTime, LocalDateTime.now())
                .set(SaleOrderEntity::getUpdateUser, StpUtil.getLoginIdAsLong())
                .eq(SaleOrderEntity::getCode, outbound.getOrderNo())
                .update();

        this.outboundMxService.saveBatch(list);
        return SaResult.data(entity);
    }

    @Override
    public SaResult updateOutbound(OutboundVo outboundVo) {
        Long id = outboundVo.getId();
        long l = CheckToolUtil.checkCodeExist(this, id, outboundVo.getCode());
        if (l > 0L) {
            return SaResult.error("发货单号重复");
        }

        this.outboundMxService.lambdaUpdate().eq(OutboundMxEntity::getOutboundId, id).remove();
        List<OutboundMxEntity> mxs = outboundVo.getMxs();
        mxs.forEach(item -> item.setOutboundId(outboundVo.getId()));
        this.outboundMxService.saveBatch(mxs);

        OutboundEntity outbound = Convert.convert(OutboundEntity.class, outboundVo);
        this.updateById(outbound);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteOutbound(List<Long> idList) {
        Long id = idList.get(0);
        OutboundEntity byId = this.getById(id);
        if (byId.getType() == 10) {
            List<OutboundMxEntity> list = this.outboundMxService.lambdaQuery()
                    .eq(OutboundMxEntity::getOutboundId, id)
                    .list();

            Map<Long, BigDecimal> map = list.stream().collect(Collectors.toMap(OutboundMxEntity::getGoodsId,
                    OutboundMxEntity::getQty));
            this.saleOrderProductService.modifyBatchById2(map, byId.getOrderNo());

            List<SaleOrderProductEntity> products = this.saleOrderProductService.getBySaleOrderCode(byId.getOrderNo());
            int sign = 0, flag = 0;
            for (SaleOrderProductEntity product : products) {
                if (product.getRealityQty().compareTo(new BigDecimal(0)) == 0) {
                    sign++;
                } else if (product.getQty().compareTo(product.getRealityQty()) <= 0) {
                    flag++;
                }
            }

            if (products.size() == sign) {
                sign = 0;
            } else if (products.size() == flag) {
                sign = 2;
            } else {
                sign = 1;
            }

            this.saleOrderService.lambdaUpdate()
                    .set(SaleOrderEntity::getSign, sign)
                    .eq(SaleOrderEntity::getCode, byId.getOrderNo())
                    .update();
        }

        this.outboundMxService.lambdaUpdate().eq(OutboundMxEntity::getOutboundId, id).remove();
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private SaResult scrap(Integer type, OutboundVo outboundVo, Integer sign) {
        Long id = outboundVo.getId();
        List<OutboundMxEntity> list;
        if (type == 20) {
            BigDecimal residueQty = this.storageService.getReturns(outboundVo.getOrderNo());
            list = this.baseMapper.getMxs(id, sign);
            if (CollUtil.isNotEmpty(list)) {
                list.get(0).setStorageQty(residueQty);
            }
        } else {
            list = this.baseMapper.getMxs(id, sign);
        }

        if (CollUtil.isEmpty(list)) {
            return SaResult.ok();
        }

        for (OutboundMxEntity mx : list) {
            if (mx.getType() < 2) {
                ProductVo product = this.proFromRedis.getProduct(mx.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    mx.setGoodsCode(product.getCode());
                    mx.setGoodsTitle(product.getTitle());
                }
            } else {
                MaterialVo material = this.proFromRedis.getMaterial(mx.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    mx.setGoodsCode(material.getCode());
                    mx.setGoodsTitle(material.getTitle());
                }
            }
        }

        outboundVo.setMxs(list);
        return SaResult.data(outboundVo);
    }

    private SaResult nonScrap(OutboundVo outboundVo, Integer sign) {
        List<OutboundMxEntity> mxs = this.baseMapper.getMxs(outboundVo.getId(), sign);
        if (CollUtil.isEmpty(mxs)) {
            return SaResult.ok();
        }

        // 获取对应的库存数据
        Set<Long> goodsIds = mxs.stream().map(OutboundMxEntity::getGoodsId).collect(Collectors.toSet());
        List<CountVo> vos = this.storageService.getKcl(goodsIds, null);

        for (OutboundMxEntity mx : mxs) {
            if (mx.getType() < 2) {
                ProductVo productVo = this.proFromRedis.getProduct(mx.getGoodsId());
                if (ObjUtil.isNotNull(productVo)) {
                    mx.setGoodsCode(productVo.getCode());
                    mx.setGoodsTitle(productVo.getTitle());
                }
            } else {
                MaterialVo materialVo = this.proFromRedis.getMaterial(mx.getGoodsId());
                if (ObjUtil.isNotNull(materialVo)) {
                    mx.setGoodsCode(materialVo.getCode());
                    mx.setGoodsTitle(materialVo.getTitle());
                }
            }

            vos.stream()
                    .filter(vo -> vo.getAId().equals(mx.getGoodsId()))
                    .findFirst()
                    .ifPresent(ip -> mx.setStorageQty(ip.getQty()));
        }

        outboundVo.setMxs(mxs);
        return SaResult.data(outboundVo);
    }

}

