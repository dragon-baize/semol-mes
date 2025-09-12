package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.BomEntity;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.WorkmanshipEntity;
import com.senmol.mes.produce.entity.WorkmanshipMx;
import com.senmol.mes.produce.mapper.WorkmanshipMapper;
import com.senmol.mes.produce.service.BomService;
import com.senmol.mes.produce.service.ProcessService;
import com.senmol.mes.produce.service.WorkmanshipMxService;
import com.senmol.mes.produce.service.WorkmanshipService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.LineVo;
import com.senmol.mes.produce.vo.WorkmanshipPojo;
import com.senmol.mes.produce.vo.WorkmanshipVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生产工艺(Workmanship)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
@Service("workmanshipService")
public class WorkmanshipServiceImpl extends ServiceImpl<WorkmanshipMapper, WorkmanshipEntity> implements WorkmanshipService {

    @Resource
    private ProFromRedis proFromRedis;
    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private ProcessService processService;
    @Resource
    private BomService bomService;
    @Resource
    private WorkmanshipMxService workmanshipMxService;

    @Override
    public List<WorkmanshipPojo> getUnboundList(Long productLineId) {
        List<WorkmanshipPojo> list = this.baseMapper.getUnboundList(productLineId);
        list.forEach(item -> {
            String[] split = item.getWmsVersions().split(",");
            item.setWmsVersions(Arrays.toString(split));
        });

        return list;
    }

    @Override
    public SaResult selectAll(Page<WorkmanshipPojo> page, WorkmanshipEntity workmanship) {
        List<WorkmanshipPojo> list = this.baseMapper.selectAll(page, workmanship);
        list.forEach(item -> {
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
    public SaResult selectOne(Long id) {
        WorkmanshipPojo pojo = this.baseMapper.getPojoById(id);
        List<ProcessEntity> processes = this.processService.getListByWorkmanshipId(id, pojo.getVersion());
        pojo.setProcesses(processes);
        return SaResult.data(pojo);
    }

    @Override
    public WorkmanshipVo getVoById(String id, String version) {
        return this.baseMapper.getVoById(id, version);
    }

    @Override
    public SaResult insertWorkmanship(WorkmanshipPojo pojo) {
        long l = checkTitle(pojo);
        if (l > 0L) {
            return SaResult.error("工艺名称重复");
        }

        List<ProcessEntity> processes = pojo.getProcesses();
        // 工位-工序一对一
        Set<Long> set = processes.stream().map(ProcessEntity::getStationId).collect(Collectors.toSet());
        if (processes.size() != set.size()) {
            return SaResult.error("存在重复工位");
        }

        WorkmanshipEntity workmanship = Convert.convert(WorkmanshipEntity.class, pojo);
        // 保存工艺数据
        boolean b = this.save(workmanship);
        if (b) {
            Long id = workmanship.getId();
            WorkmanshipMx mx = Convert.convert(WorkmanshipMx.class, pojo);
            mx.setPid(id);
            boolean save = this.workmanshipMxService.save(mx);
            if (save) {
                // 添加到缓存
                pojo.setCreateTime(workmanship.getCreateTime());
                pojo.setCreateUser(workmanship.getCreateUser());
                this.proAsyncUtil.dealWorkmanship(pojo, id, 1);
            }
        }

        return SaResult.data(workmanship);
    }

    @Override
    public SaResult updateWorkmanship(WorkmanshipPojo pojo) {
        long l = checkTitle(pojo);
        if (l > 0L) {
            return SaResult.error("工艺名称重复");
        }

        List<ProcessEntity> processes = pojo.getProcesses();
        // 工位-工序一对一
        Set<Long> set = processes.stream().map(ProcessEntity::getStationId).collect(Collectors.toSet());
        if (processes.size() != set.size()) {
            return SaResult.error("存在重复工位");
        }

        // 更新工艺数据
        WorkmanshipEntity workmanship = Convert.convert(WorkmanshipEntity.class, pojo);
        boolean b = this.updateById(workmanship);
        if (b) {
            Long id = workmanship.getId();
            Integer version = workmanship.getVersion();
            WorkmanshipMx mx = Convert.convert(WorkmanshipMx.class, pojo);
            mx.setPid(id);
            mx.setWmsVersion(version);
            boolean save = this.workmanshipMxService.save(mx);
            if (save) {
                // 添加到缓存
                pojo.setCreateTime(workmanship.getUpdateTime());
                pojo.setCreateUser(workmanship.getUpdateUser());
                this.proAsyncUtil.dealWorkmanship(pojo, id, version);
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult copyWorkmanship(Long id) {
        // 获取工艺
        WorkmanshipPojo pojo = this.baseMapper.getPojoById(id);
        if (pojo.getStatus() != 0) {
            return SaResult.error("工艺未审批通过");
        }

        String title = "Copy:" + pojo.getTitle();
        Long count = this.lambdaQuery().eq(WorkmanshipEntity::getTitle, title).count();
        if (count > 1L) {
            title = title + "_" + count;
        }

        WorkmanshipEntity workmanship = new WorkmanshipEntity();
        workmanship.setTitle(title);
        boolean b = this.save(workmanship);
        if (b) {
            Long workmanshipId = workmanship.getId();
            WorkmanshipMx mx = Convert.convert(WorkmanshipMx.class, pojo);
            mx.setPid(workmanshipId);
            mx.setWmsVersion(1);
            boolean save = this.workmanshipMxService.save(mx);
            if (save) {
                // 添加到缓存
                pojo.setCreateTime(workmanship.getCreateTime());
                pojo.setCreateUser(workmanship.getCreateUser());
                this.proAsyncUtil.copyWorkmanship(pojo, workmanshipId);
            }
        }

        return SaResult.ok("复制成功！");
    }

    @Override
    public SaResult deleteWorkmanship(Long id) {
        long count = this.bomService.lambdaQuery().eq(BomEntity::getWorkmanshipId, id).last(CheckToolUtil.LIMIT).count();
        if (count > 0L) {
            return SaResult.error("工艺已绑定清单");
        }

        List<WorkmanshipMx> list = this.workmanshipMxService.lambdaQuery().eq(WorkmanshipMx::getPid, id).list();
        this.workmanshipMxService.lambdaUpdate().eq(WorkmanshipMx::getPid, id).remove();
        this.removeById(id);
        // 删除缓存
        this.proAsyncUtil.delWorkmanship(list, id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private long checkTitle(WorkmanshipPojo pojo) {
        LambdaQueryWrapper<WorkmanshipEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkmanshipEntity::getTitle, pojo.getTitle());

        Long id = pojo.getId();
        if (ObjUtil.isNotNull(id)) {
            wrapper.ne(WorkmanshipEntity::getId, id);
        }

        wrapper.last(CheckToolUtil.LIMIT);
        return this.count(wrapper);
    }

}

