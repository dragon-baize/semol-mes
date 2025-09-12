package com.senmol.mes.quality.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.StationBadModeEntity;
import com.senmol.mes.produce.service.StationBadModeService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.LineVo;
import com.senmol.mes.quality.entity.BadModeEntity;
import com.senmol.mes.quality.mapper.BadModeMapper;
import com.senmol.mes.quality.service.BadModeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 不良模式(BadMode)表服务实现类
 *
 * @author makejava
 * @since 2023-01-31 09:15:55
 */
@Service("badModeService")
public class BadModeServiceImpl extends ServiceImpl<BadModeMapper, BadModeEntity> implements BadModeService {

    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private StationBadModeService stationBadModeService;
    @Resource
    private RedisService redisService;

    @Override
    public SaResult selectAll(Page<BadModeEntity> page, BadModeEntity badMode) {
        this.page(page, new QueryWrapper<>(badMode));
        List<BadModeEntity> list = page.getRecords();
        // 产线名称设置
        list.forEach(item -> {
            LineVo line = this.proFromRedis.getLine(item.getProductLineId());
            if (ObjUtil.isNotNull(line)) {
                item.setProductLineCode(line.getCode());
                item.setProductLineTitle(line.getTitle());
            }
        });

        return SaResult.data(page);
    }

    @Override
    public SaResult insertBadMode(BadModeEntity badMode) {
        long l = CheckToolUtil.checkCodeExist(this, null, badMode.getCode());
        if (l > 0L) {
            return SaResult.error("不良模式编号重复");
        }

        this.save(badMode);
        // 添加到缓存
        this.proAsyncUtil.dealBadMode(badMode);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateBadMode(BadModeEntity badMode) {
        long l = CheckToolUtil.checkCodeExist(this, badMode.getId(), badMode.getCode());
        if (l > 0L) {
            return SaResult.error("不良模式编号重复");
        }

        this.updateById(badMode);
        // 添加到缓存
        this.proAsyncUtil.dealBadMode(badMode);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult delBadMode(Long id) {
        List<StationBadModeEntity> stationBadModes = this.stationBadModeService.lambdaQuery()
                .eq(StationBadModeEntity::getBadModeId, id)
                .list();

        if (CollUtil.isNotEmpty(stationBadModes)) {
            return SaResult.error("不良模式已绑定工位");
        }

        this.removeById(id);
        // 删除缓存
        this.redisService.del(RedisKeyEnum.QUALITY_BAD_MODE.getKey() + id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

