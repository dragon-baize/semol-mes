package com.senmol.mes.system.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.system.entity.DictEntity;
import com.senmol.mes.system.mapper.DictMapper;
import com.senmol.mes.system.service.DictService;
import com.senmol.mes.system.utils.SysAsyncUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 字典(Dict)表服务实现类
 *
 * @author makejava
 * @since 2023-01-03 17:03:18
 */
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, DictEntity> implements DictService {

    @Resource
    private SysAsyncUtil sysAsyncUtil;

    @Override
    public SaResult getMx(String title) {
        return SaResult.data(this.baseMapper.getMx(title));
    }

    @Override
    public SaResult deleteDict(Long id) {
        this.removeById(id);
        // 处理字典明细缓存数据
        this.sysAsyncUtil.delDict(id);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }
}

