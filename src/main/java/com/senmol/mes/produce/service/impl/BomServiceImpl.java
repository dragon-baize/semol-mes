package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.exception.BusinessException;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.common.utils.CountVo;
import com.senmol.mes.produce.entity.BomEntity;
import com.senmol.mes.produce.entity.BomMaterialEntity;
import com.senmol.mes.produce.entity.WorkmanshipEntity;
import com.senmol.mes.produce.mapper.BomMapper;
import com.senmol.mes.produce.service.BomMaterialService;
import com.senmol.mes.produce.service.BomService;
import com.senmol.mes.produce.service.WorkmanshipService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料清单(Bom)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:08
 */
@Service("bomService")
public class BomServiceImpl extends ServiceImpl<BomMapper, BomEntity> implements BomService {

    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private BomMaterialService bomMaterialService;
    @Resource
    private RedisService redisService;
    @Resource
    private WorkmanshipService workmanshipService;

    @Override
    public SaResult selectAll(Page<BomEntity> page, BomEntity bom) {
        this.baseMapper.selectAll(page, bom);
        List<BomEntity> records = page.getRecords();
        for (BomEntity record : records) {
            LineVo line = this.proFromRedis.getLine(record.getProductLineId());
            if (ObjUtil.isNotNull(line)) {
                record.setProductLineCode(line.getCode());
                record.setProductLineTitle(line.getTitle());
            }

            WorkmanshipVo workmanshipVo = this.proFromRedis.getWorkmanship(record.getWorkmanshipId() + ":v" + record.getWmsVersion());
            if (ObjUtil.isNotNull(workmanshipVo)) {
                record.setWorkmanshipTitle(workmanshipVo.getTitle());
            }
        }

        return SaResult.data(page);
    }

    @Override
    public SaResult selectOne(Long id) {
        BomEntity bom = this.getById(id);

        List<BomMaterialEntity> list = this.bomMaterialService.lambdaQuery().eq(BomMaterialEntity::getBomId, id).list();
        for (BomMaterialEntity bomMaterial : list) {
            setMaterialValue(bomMaterial, this.proFromRedis, this.sysFromRedis);
        }

        bom.setMaterials(list);
        return SaResult.data(bom);
    }

    @Override
    public List<CountVo> getProductId(Long materialId) {
        return this.baseMapper.getProductId(materialId);
    }

    @Override
    public SaResult insertBom(BomEntity bom) {
        // 一个产品一个清单
        long count = this.lambdaQuery().eq(BomEntity::getProductId, bom.getProductId()).last(CheckToolUtil.LIMIT).count();
        if (count > 0L) {
            return SaResult.error("重复绑定的产品数据");
        }

        List<BomMaterialEntity> materials = this.checkBom(bom);
        boolean b = this.save(bom);
        if (b) {
            this.dealBom(bom);
            this.bomMaterialService.saveBatch(materials);
        }

        return SaResult.data(bom);
    }

    @Override
    public SaResult updateBom(BomEntity bom) {
        List<BomMaterialEntity> materials = this.checkBom(bom);
        boolean b = this.updateById(bom);
        if (b) {
            this.bomMaterialService.lambdaUpdate().eq(BomMaterialEntity::getBomId, bom.getId()).remove();

            this.dealBom(bom);
            this.bomMaterialService.saveBatch(materials);
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteBom(Long id) {
        BomEntity bom = this.getById(id);
        if (BeanUtil.isEmpty(bom)) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        // 删除清单-物料表数据（物理删除）
        this.bomMaterialService.lambdaUpdate().eq(BomMaterialEntity::getBomId, id).remove();
        this.removeById(id);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.PRODUCE_BOM.getKey() + bom.getProductId());
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private List<BomMaterialEntity> checkBom(BomEntity bom) {
        List<BomMaterialEntity> materials = bom.getMaterials();

        List<Long> ids = materials.stream()
                .filter(item -> item.getType() == 1)
                .map(BomMaterialEntity::getMaterialId)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)) {
            List<String> productCodes = this.baseMapper.unboundBomProduct(ids);
            if (CollUtil.isNotEmpty(productCodes)) {
                throw new BusinessException("存在未设置清单的半成品，编号为：" + productCodes);
            }
        }

        WorkmanshipEntity workmanship = this.workmanshipService.getById(bom.getWorkmanshipId());
        bom.setWmsVersion(workmanship.getVersion());
        return materials;
    }

    static void setMaterialValue(BomMaterialEntity bomMaterial, ProFromRedis proFromRedis, SysFromRedis sysFromRedis) {
        if (bomMaterial.getType() == 0) {
            MaterialVo materialVo = proFromRedis.getMaterial(bomMaterial.getMaterialId());
            if (ObjUtil.isNotNull(materialVo)) {
                bomMaterial.setMaterialCode(materialVo.getCode());
                bomMaterial.setMaterialTitle(materialVo.getTitle());
                bomMaterial.setUnitTitle(sysFromRedis.getDictMx(materialVo.getUnitId()));
            }
        } else {
            ProductVo productVo = proFromRedis.getProduct(bomMaterial.getMaterialId());
            if (ObjUtil.isNotNull(productVo)) {
                bomMaterial.setMaterialCode(productVo.getCode());
                bomMaterial.setMaterialTitle(productVo.getTitle());
                bomMaterial.setUnitTitle(sysFromRedis.getDictMx(productVo.getUnitId()));
            }
        }

        ProcessVo processVo = proFromRedis.getProcess(bomMaterial.getProcessId());
        if (ObjUtil.isNotNull(processVo)) {
            bomMaterial.setProcessTitle(processVo.getTitle());
        }
    }

    private void dealBom(BomEntity bom) {
        List<BomMaterialEntity> bomMaterials = bom.getMaterials();
        BomVo bomVo = Convert.convert(BomVo.class, bom);
        bomVo.setStatus(0);

        this.finished(bomVo, bomMaterials, bom.getCreateTime(), bom.getCreateUser());

        this.redisService.set(RedisKeyEnum.PRODUCE_BOM.getKey() + bomVo.getProductId(), bomVo,
                RedisKeyEnum.PRODUCE_BOM.getTimeout());
    }

    private void finished(BomVo bomVo, List<BomMaterialEntity> bomMaterials, LocalDateTime creatTime, Long createUser) {
        List<BomMaterialVo> materialVos = new ArrayList<>(bomMaterials.size() + 5);

        Long workmanshipId = bomVo.getWorkmanshipId();
        if (ObjectUtil.isNull(workmanshipId)) {
            for (BomMaterialEntity bomMaterial : bomMaterials) {
                bomMaterial.setBomId(bomVo.getId());
                bomMaterial.setCreateTime(creatTime);
                bomMaterial.setCreateUser(createUser);

                BomMaterialVo materialVo = Convert.convert(BomMaterialVo.class, bomMaterial);
                materialVos.add(materialVo);
            }
        } else {
            WorkmanshipVo workmanshipVo = this.proFromRedis.getWorkmanship(workmanshipId + ":v" + bomVo.getWmsVersion());
            if (ObjUtil.isNull(workmanshipVo)) {
                return;
            }

            List<Long> processIds = workmanshipVo.getProcessIds();
            for (BomMaterialEntity bomMaterial : bomMaterials) {
                bomMaterial.setBomId(bomVo.getId());
                bomMaterial.setCreateTime(creatTime);
                bomMaterial.setCreateUser(createUser);

                // 获取工序对应的工位、设备
                Long processId = bomMaterial.getProcessId();
                ProcessVo process = this.proFromRedis.getProcess(processId);
                if (ObjUtil.isNull(process)) {
                    continue;
                }

                Long stationId = process.getStationId();
                StationVo station = this.proFromRedis.getStation(stationId);

                BomMaterialVo bomMaterialVo = Convert.convert(BomMaterialVo.class, bomMaterial);
                bomMaterialVo.setStationId(stationId);
                bomMaterialVo.setDeviceId(ObjUtil.isNull(station) ? null : station.getDeviceId());
                materialVos.add(bomMaterialVo);

                processIds.remove(processId);
            }

            // 工艺中为在清单中设置的工序也添加到清单的缓存中
            if (CollUtil.isNotEmpty(processIds)) {
                BomMaterialVo materialVo;
                for (Long processId : processIds) {
                    ProcessVo process = this.proFromRedis.getProcess(processId);
                    if (ObjUtil.isNull(process)) {
                        continue;
                    }

                    Long stationId = process.getStationId();
                    StationVo station = this.proFromRedis.getStation(stationId);

                    materialVo = new BomMaterialVo();
                    materialVo.setProcessId(processId);
                    materialVo.setSerialNo(process.getSerialNo());
                    materialVo.setStationId(stationId);
                    materialVo.setDeviceId(ObjUtil.isNull(station) ? null : station.getDeviceId());
                    materialVo.setType(null);
                    materialVos.add(materialVo);
                }
            }
        }

        bomVo.setMaterialVos(materialVos);
    }

}

