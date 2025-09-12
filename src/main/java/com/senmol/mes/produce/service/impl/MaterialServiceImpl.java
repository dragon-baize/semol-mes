package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.BomMaterialEntity;
import com.senmol.mes.produce.entity.MaterialEntity;
import com.senmol.mes.produce.entity.PackMaterialEntity;
import com.senmol.mes.produce.mapper.MaterialMapper;
import com.senmol.mes.produce.service.BomMaterialService;
import com.senmol.mes.produce.service.MaterialService;
import com.senmol.mes.produce.service.PackMaterialService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.system.entity.DictEntity;
import com.senmol.mes.system.entity.DictMxEntity;
import com.senmol.mes.system.service.DictMxService;
import com.senmol.mes.system.service.DictService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.service.StorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料管理(Material)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("materialService")
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, MaterialEntity> implements MaterialService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private RedisService redisService;
    @Resource
    private BomMaterialService bomMaterialService;
    @Resource
    private PackMaterialService packMaterialService;
    @Resource
    private DictService dictService;
    @Resource
    private DictMxService dictMxService;
    @Resource
    private StorageService storageService;

    @Override
    public List<MaterialVo> getList(Long supplierId, Integer type) {
        return this.baseMapper.getList(supplierId, type);
    }

    @Override
    public SaResult selectAll(Page<MaterialEntity> page, MaterialEntity material) {
        this.page(page, new QueryWrapper<>(material));
        List<MaterialEntity> records = page.getRecords();
        records.forEach(item -> item.setUnitTitle(this.sysFromRedis.getDictMx(item.getUnitId())));
        return SaResult.data(page);
    }

    @Override
    public SaResult selectOne(Long id) {
        MaterialEntity material = this.getById(id);
        material.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
        return SaResult.data(material);
    }

    @Override
    public SaResult insertMaterial(MaterialEntity material) {
        long l = CheckToolUtil.checkCodeExist(this, null, material.getCode());
        if (l > 0L) {
            return SaResult.error("物料编号重复");
        }

        this.save(material);
        // 添加到缓存
        this.proAsyncUtil.dealMaterial(material);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public List<Object> insertByExcel(List<Object> cachedDataList) {
        // 获取所有缓存单位
        Map<Object, Object> map = this.getDictMxs();

        // 错误信息展示
        List<Object> failList = new ArrayList<>(cachedDataList.size() / 4);

        // 获取所有物料/产品信息
        List<String> codes = this.getMaterialCodes();

        List<MaterialEntity> materials = new ArrayList<>(cachedDataList.size());
        for (Object object : cachedDataList) {
            MaterialVo vo = Convert.convert(MaterialVo.class, object);

            if (codes.contains(vo.getCode())) {
                vo.setRemarks("物料编号重复【" + vo.getCode() + "】");
                failList.add(vo);
                continue;
            }

            Object o = map.get(vo.getUnitTitle());
            if (ObjectUtil.isNull(o)) {
                vo.setRemarks("单位字典中不存在该物料单位");
                failList.add(vo);
                continue;
            }

            vo.setId(null);
            vo.setUnitId(Long.parseLong(map.get(vo.getUnitTitle()).toString()));
            materials.add(Convert.convert(MaterialEntity.class, vo));

            // 物料编号加入缓存
            codes.add(vo.getCode());
        }

        this.saveBatch(materials);
        // 物料编号加入缓存
        this.redisService.set(RedisKeyEnum.PRODUCE_MATERIAL_CODES.getKey(), codes,
                RedisKeyEnum.PRODUCE_MATERIAL_CODES.getTimeout());
        return failList;
    }

    @Override
    public SaResult updateMaterial(MaterialEntity material) {
        long l = CheckToolUtil.checkCodeExist(this, material.getId(), material.getCode());
        if (l > 0L) {
            return SaResult.error("物料编号重复");
        }

        this.updateById(material);
        // 添加到缓存
        this.proAsyncUtil.dealMaterial(material);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult delMaterial(Long id) {
        long count = this.bomMaterialService.lambdaQuery()
                .eq(BomMaterialEntity::getMaterialId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("物料已绑定清单");
        }

        count = this.storageService.lambdaQuery()
                .eq(StorageEntity::getGoodsId, id)
                .gt(StorageEntity::getStatus, 0)
                .gt(StorageEntity::getResidueQty, 0)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("物料存在库存数据");
        }

        // 删除包装-物料表数据
        this.packMaterialService.lambdaUpdate().eq(PackMaterialEntity::getMaterialId, id).remove();

        // 删除缓存
        this.removeById(id);
        this.redisService.del(RedisKeyEnum.PRODUCE_MATERIAL.getKey() + id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private Map<Object, Object> getDictMxs() {
        Map<Object, Object> map = this.redisService.hGetAll(RedisKeyEnum.SYS_DICT_MX_ALL.getKey());
        if (CollUtil.isEmpty(map)) {
            DictEntity dict = this.dictService.lambdaQuery().eq(DictEntity::getTitle, "单位").one();

            map = this.dictMxService.lambdaQuery()
                    .eq(DictMxEntity::getPid, dict.getId())
                    .list()
                    .stream()
                    .collect(Collectors.toMap(DictMxEntity::getTitle, DictMxEntity::getId));

            this.redisService.hSetAll(RedisKeyEnum.SYS_DICT_MX_ALL.getKey(), map,
                    RedisKeyEnum.SYS_DICT_MX_ALL.getTimeout());
        }

        return map;
    }

    private List<String> getMaterialCodes() {
        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_MATERIAL_CODES.getKey());
        List<String> codes = Convert.toList(String.class, object);
        if (CollUtil.isNotEmpty(codes)) {
            return codes;
        }

        codes = this.baseMapper.getList(null, null)
                .stream()
                .map(MaterialVo::getCode)
                .collect(Collectors.toList());
        this.redisService.set(RedisKeyEnum.PRODUCE_MATERIAL_CODES.getKey(), codes,
                RedisKeyEnum.PRODUCE_MATERIAL_CODES.getTimeout());
        return codes;
    }

}

