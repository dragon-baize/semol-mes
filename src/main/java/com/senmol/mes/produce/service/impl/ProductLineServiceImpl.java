package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.DeviceEntity;
import com.senmol.mes.produce.entity.ProductEntity;
import com.senmol.mes.produce.entity.ProductLineEntity;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.mapper.ProductLineMapper;
import com.senmol.mes.produce.service.DeviceService;
import com.senmol.mes.produce.service.ProductLineService;
import com.senmol.mes.produce.service.ProductService;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.system.service.RoleService;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 产线管理(ProductLine)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("productLineService")
public class ProductLineServiceImpl extends ServiceImpl<ProductLineMapper, ProductLineEntity> implements ProductLineService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private RedisService redisService;
    @Resource
    private StationService stationService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private ProductService productService;
    @Resource
    private RoleService roleService;
    @Resource
    private ProFromRedis proFromRedis;

    @Override
    public List<ProductInfo> selectById(Long productLineId) {
        List<WorkOrderMxProcessVo> vos = this.baseMapper.processStatus(productLineId);
        if (CollUtil.isEmpty(vos)) {
            return null;
        }

        List<ProductInfo> list = new ArrayList<>();
        boolean flag = false;
        for (WorkOrderMxProcessVo vo : vos) {
            this.setValue(vo);

            ProductInfo info = list.stream()
                    .filter(item -> item.getId().equals(vo.getPlanId()))
                    .findFirst()
                    .orElse(null);

            if (BeanUtil.isEmpty(info)) {
                flag = true;
                info = Convert.convert(ProductInfo.class, vo);
                info.setId(vo.getPlanId());
            }

            MergesInfo merges = Convert.convert(MergesInfo.class, vo);
            merges.setId(vo.getProcessId());

            List<MergesInfo> mergeInfos = info.getMergeInfos();
            if (mergeInfos == null) {
                mergeInfos = new ArrayList<>();
            }

            mergeInfos.add(merges);

            if (flag) {
                info.setMergeInfos(mergeInfos);
                list.add(info);
                flag = false;
            }
        }

        return list;
    }

    @Override
    public Page<ProductLineEntity> selectAll(Page<ProductLineEntity> page, ProductLineEntity productLine) {
        page = this.page(page, new QueryWrapper<>(productLine));
        List<ProductLineEntity> records = page.getRecords();
        records.forEach(item -> item.setStatusTitle(this.sysFromRedis.getDictMx(item.getStatus())));
        return page;
    }

    @Override
    public List<ProductLineEntity> getByUserId(Long userId) {
        // 管理员角色显示所有产线
        boolean result = this.roleService.isAdmin(userId);
        if (result) {
            userId = null;
        }

        return this.baseMapper.getByUserId(userId);
    }

    @Override
    public BigDecimal getAllTotal() {
        return this.baseMapper.getAllTotal();
    }

    @Override
    public SaResult insertLine(ProductLineEntity productLine) {
        long l = CheckToolUtil.checkCodeExist(this, null, productLine.getCode());
        if (l > 0L) {
            return SaResult.error("产线编号重复");
        }

        this.save(productLine);
        // 添加到缓存
        this.proAsyncUtil.dealLine(productLine);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateLine(ProductLineEntity productLine) {
        long l = CheckToolUtil.checkCodeExist(this, productLine.getId(), productLine.getCode());
        if (l > 0L) {
            return SaResult.error("产线编号重复");
        }

        this.updateById(productLine);
        // 更新缓存
        this.proAsyncUtil.dealLine(productLine);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteLine(Long id) {
        Long count = this.deviceService.lambdaQuery()
                .eq(DeviceEntity::getProductLineId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("产线已绑定设备");
        }

        count = this.stationService.lambdaQuery()
                .eq(StationEntity::getProductLineId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("产线已绑定工位");
        }

        count = this.productService.lambdaQuery()
                .eq(ProductEntity::getProductLineId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("产线已绑定产品");
        }

        // 删除缓存
        this.redisService.del(RedisKeyEnum.PRODUCE_LINE.getKey() + id);
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private void setValue(WorkOrderMxProcessVo vo) {
        ProductVo product = this.proFromRedis.getProduct(vo.getProductId());
        if (ObjUtil.isNotNull(product)) {
            vo.setProductTitle(product.getTitle());
            vo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
        }

        StationVo station = this.proFromRedis.getStation(vo.getStationId());
        if (ObjUtil.isNotNull(station)) {
            vo.setStationCode(station.getCode());
            vo.setStationTitle(station.getTitle());
        }

        ProcessVo process = this.proFromRedis.getProcess(vo.getProcessId());
        if (ObjUtil.isNotNull(process)) {
            vo.setProcessTitle(process.getTitle());
        }

        DeviceVo device = this.proFromRedis.getDevice(vo.getDeviceId());
        if (ObjUtil.isNotNull(device)) {
            vo.setDeviceCode(device.getCode());
            vo.setDeviceTitle(device.getTitle());
        }
    }

}

