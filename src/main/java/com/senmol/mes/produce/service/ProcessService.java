package com.senmol.mes.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.produce.entity.ProcessEntity;
import com.senmol.mes.produce.vo.ProcessVo;

import java.util.List;

/**
 * 工序管理(Process)表服务接口
 *
 * @author makejava
 * @since 2023-01-29 14:45:10
 */
public interface ProcessService extends IService<ProcessEntity> {

    /**
     * 查询所有数据
     *
     * @param workmanshipId 工艺ID
     * @param wmsVersion    工艺版本号
     * @return 所有数据
     */
    List<ProcessEntity> getUnboundList(Long workmanshipId, Integer wmsVersion);

    /**
     * 查询工艺工序
     *
     * @param id         工艺ID
     * @param wmsVersion 工艺版本号
     * @return 工序列表
     */
    List<ProcessEntity> getListByWorkmanshipId(Long id, Integer wmsVersion);

    /**
     * 产品查工序
     *
     * @param productId 产品ID
     * @return 工序列表
     */
    List<ProcessEntity> byProductId(Long productId);

    /**
     * 查询工序
     *
     * @param id 工序ID
     * @return 工序Vo
     */
    ProcessVo getByIdOrDel(Long id);
}

