package com.senmol.mes.produce.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.StationEntity;
import com.senmol.mes.produce.vo.BomMaterialVo;

import java.util.List;
import java.util.Map;

/**
 * 工位管理(Station)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:11
 */
public interface StationService extends IService<StationEntity> {

    /**
     * 登录
     *
     * @param code     工位编号
     * @param password 密码
     * @return 结果
     */
    SaResult login(String code, String password);

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param station 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<StationEntity> page, StationEntity station);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    StationEntity selectOne(Long id);

    /**
     * 查询用户绑定工位、工序
     *
     * @param productLineId 用户ID
     * @return 工位、工序信息
     */
    SaResult getByUserId(Long productLineId);

    /**
     * 查询工位不良模式
     *
     * @param id 工位ID
     * @return 不良模式列表
     */
    Map<Long, String> getBadMode(Long id);

    /**
     * 获取工位信息
     *
     * @param processIds 工序ID
     */
    List<BomMaterialVo> getStationByProcessId(List<Long> processIds);

    /**
     * 新增数据
     *
     * @param station 实体对象
     * @return 新增结果
     */
    SaResult insertStation(StationEntity station);

    /**
     * 修改数据
     *
     * @param station 实体对象
     * @return 修改结果
     */
    SaResult updateStation(StationEntity station);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteStation(Long id);

}

