package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.DictEntity;

/**
 * 字典(Dict)表服务接口
 *
 * @author makejava
 * @since 2023-01-03 17:03:18
 */
public interface DictService extends IService<DictEntity> {
    /**
     * 获取字典明细
     *
     * @param title 字典名称
     * @return 字典明细
     */
    SaResult getMx(String title);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteDict(Long id);
}

