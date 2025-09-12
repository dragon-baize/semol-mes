package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.PackEntity;
import com.senmol.mes.produce.entity.PackMaterialEntity;
import com.senmol.mes.produce.entity.WorkmanshipMx;
import com.senmol.mes.produce.mapper.PackMapper;
import com.senmol.mes.produce.service.PackMaterialService;
import com.senmol.mes.produce.service.PackService;
import com.senmol.mes.produce.service.WorkmanshipMxService;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装管理(Pack)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
@Service("packService")
public class PackServiceImpl extends ServiceImpl<PackMapper, PackEntity> implements PackService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private PackMaterialService packMaterialService;
    @Resource
    private WorkmanshipMxService workmanshipMxService;

    @Override
    public SaResult selectAll(Page<PackEntity> page, PackEntity pack) {
        this.page(page, new QueryWrapper<>(pack));
        List<PackEntity> records = page.getRecords();
        records.forEach(item -> item.setStatusTitle(this.sysFromRedis.getDictMx(item.getStatus())));
        return SaResult.data(page);
    }

    @Override
    public PackEntity selectOne(Long id) {
        PackEntity pack = this.getById(id);
        // 获取包装-物料表数据（删除不存在的物料）
        List<PackMaterialEntity> list =
                this.packMaterialService.lambdaQuery().eq(PackMaterialEntity::getPackId, id).list();
        List<Long> materialIds = list.stream().map(PackMaterialEntity::getMaterialId).collect(Collectors.toList());
        pack.setMaterialIds(materialIds);
        return pack;
    }

    @Override
    public SaResult insertPack(PackEntity pack) {
        long l = CheckToolUtil.checkCodeExist(this, null, pack.getCode());
        if (l > 0L) {
            return SaResult.error("包装编号重复");
        }

        this.save(pack);

        // 获取物料主键列表
        List<Long> materialIds = pack.getMaterialIds();
        // 保存包装-物料数据
        if (materialIds != null && materialIds.size() > 0) {
            List<PackMaterialEntity> list = new ArrayList<>(materialIds.size());
            for (Long materialId : materialIds) {
                PackMaterialEntity entity = new PackMaterialEntity();
                entity.setPackId(pack.getId());
                entity.setMaterialId(materialId);
                list.add(entity);
            }

            this.packMaterialService.saveBatch(list);
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updatePack(PackEntity pack) {
        Long id = pack.getId();
        long l = CheckToolUtil.checkCodeExist(this, id, pack.getCode());
        if (l > 0L) {
            return SaResult.error("包装编号重复");
        }

        this.updateById(pack);

        // 删除已存在的
        this.packMaterialService.lambdaUpdate().eq(PackMaterialEntity::getPackId, id).remove();

        // 获取物料主键列表
        List<Long> materialIds = pack.getMaterialIds();
        // 保存包装-物料数据
        if (materialIds != null && materialIds.size() > 0) {
            List<PackMaterialEntity> list = new ArrayList<>(materialIds.size());
            for (Long materialId : materialIds) {
                PackMaterialEntity entity = new PackMaterialEntity();
                entity.setPackId(id);
                entity.setMaterialId(materialId);
                list.add(entity);
            }

            this.packMaterialService.saveBatch(list);
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deletePack(Long id) {
        boolean b = this.removeById(id);
        if (b) {
            this.workmanshipMxService.lambdaUpdate().set(WorkmanshipMx::getPackId, null)
                    .eq(WorkmanshipMx::getPackId, id).update();

            // 删除包装-物料表数据
            this.packMaterialService.lambdaUpdate().eq(PackMaterialEntity::getPackId, id).remove();
        }

        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

