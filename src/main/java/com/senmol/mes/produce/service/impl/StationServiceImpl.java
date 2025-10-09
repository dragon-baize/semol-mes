package com.senmol.mes.produce.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.entity.StationBadModeEntity;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.entity.StationUserEntity;
import com.senmol.mes.produce.mapper.StationMapper;
import com.senmol.mes.produce.service.ProcessService;
import com.senmol.mes.produce.service.StationBadModeService;
import com.senmol.mes.produce.service.StationService;
import com.senmol.mes.produce.service.StationUserService;
import com.senmol.mes.produce.utils.ProAsyncUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.BomMaterialVo;
import com.senmol.mes.produce.vo.DeviceVo;
import com.senmol.mes.produce.vo.StationPojo;
import com.senmol.mes.quality.vo.BadModeVo;
import com.senmol.mes.system.service.MenuService;
import com.senmol.mes.system.vo.UserRoute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工位管理(Station)表服务实现类
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
@Service("stationService")
public class StationServiceImpl extends ServiceImpl<StationMapper, StationEntity> implements StationService {

    @Lazy
    @Resource
    private ProAsyncUtil proAsyncUtil;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private ProcessService processService;
    @Resource
    private StationBadModeService stationBadModeService;
    @Resource
    private StationUserService stationUserService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Value("${login.type.station:station}")
    private String loginType;
    @Resource
    private MenuService menuService;

    @Override
    public SaResult login(String code, String password) {
        List<StationPojo> stations = this.baseMapper.getByCode(code);
        if (CollUtil.isEmpty(stations)) {
            return new SaResult(SaResult.CODE_ERROR, "工位账号不存在或未绑定工序", -1);
        }

        StationPojo station = stations.get(0);
        Long id = station.getId();
        String md5 = SaSecureUtil.md5(password);
        boolean matches = this.passwordEncoder.matches(md5, station.getPassword());
        if (!matches) {
            return new SaResult(SaResult.CODE_ERROR, "密码错误", id);
        }

        StpUtil.login(id);
        SaSession session = StpUtil.getSessionByLoginId(id);
        session.set("tag", loginType);

        // 封装账号数据返回
        Map<String, Object> map = MapUtil.newHashMap(3);
        map.put("userInfo", station);
        // 账号token
        map.put("tokenInfo", StpUtil.getTokenInfo());
        // 查询工位菜单
        List<UserRoute> trees = this.menuService.getByStationId(id);
        map.put("menuTreeInfo", trees);
        return SaResult.data(map);
    }

    @Override
    public SaResult selectAll(Page<StationEntity> page, StationEntity station) {
        this.page(page, new QueryWrapper<>(station));
        List<StationEntity> records = page.getRecords();
        records.forEach(item -> {
            DeviceVo device = this.proFromRedis.getDevice(item.getDeviceId());
            if (ObjUtil.isNotNull(device)) {
                item.setDeviceTitle(device.getTitle());
            }
        });

        return SaResult.data(page);
    }

    @Override
    public StationEntity selectOne(Long id) {
        StationEntity station = this.getById(id);

        // 不良模式数据
        List<StationBadModeEntity> badModes =
                this.stationBadModeService.lambdaQuery().eq(StationBadModeEntity::getStationId, id).list();
        List<Long> badModeIds =
                badModes.stream().map(StationBadModeEntity::getBadModeId).collect(Collectors.toList());
        station.setBadModeIds(badModeIds);

        // 人员数据
        List<StationUserEntity> users =
                this.stationUserService.lambdaQuery().eq(StationUserEntity::getStationId, id).list();
        List<Long> userIds = users.stream().map(StationUserEntity::getUserId).collect(Collectors.toList());
        station.setUserIds(userIds);

        return station;
    }

    @Override
    public SaResult getByUserId(Long productLineId) {
        return SaResult.data(this.baseMapper.getByUserId(productLineId, StpUtil.getLoginIdAsLong()));
    }

    @Override
    public Map<Long, String> getBadMode(Long id) {
        // 不良模式数据
        List<StationBadModeEntity> badModes =
                this.stationBadModeService.lambdaQuery().eq(StationBadModeEntity::getStationId, id).list();

        Map<Long, String> map = MapUtil.newHashMap(badModes.size());
        for (StationBadModeEntity stationBadMode : badModes) {
            Long badModeId = stationBadMode.getBadModeId();
            BadModeVo badModeVo = this.proFromRedis.getBadMode(badModeId);
            if (ObjUtil.isNotNull(badModeVo)) {
                map.put(badModeId, badModeVo.getTitle());
            }
        }

        return map;
    }

    @Override
    public List<BomMaterialVo> getStationByProcessId(List<Long> processIds) {
        return this.baseMapper.getStationByProcessId(processIds);
    }

    @Override
    public SaResult insertStation(StationEntity station) {
        String date = LocalDate.now().toString();
        int count = this.baseMapper.getTodayCount(date);
        station.setCode("GW" + date.replace("-", "") + (101 + count * 3));

        String md5 = SaSecureUtil.md5("123456");
        String encode = this.passwordEncoder.encode(md5);
        station.setPassword(encode);

        // 保存工位数据
        this.save(station);

        // 添加到缓存
        this.proAsyncUtil.dealStation(station, 0);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateStation(StationEntity station) {
        Long id = station.getId();
        long count = this.processService.lambdaQuery()
                .eq(ProcessEntity::getStationId, id)
                .last(CheckToolUtil.LIMIT)
                .count();

        StationEntity entity = this.getById(id);
        if (count > 0L && !entity.getTitle().equals(station.getTitle())) {
            return SaResult.error("绑定工序的工位名称不允许变更");
        }

        // 更新工位数据
        this.updateById(station);
        // 添加到缓存
        this.proAsyncUtil.dealStation(station, 1);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteStation(Long id) {
        long count = this.processService.lambdaQuery()
                .eq(ProcessEntity::getStationId, id)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("工位已绑定工序");
        }

        this.proAsyncUtil.delStation(id);
        this.removeById(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

}

