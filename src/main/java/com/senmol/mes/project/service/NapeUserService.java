package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.NapeUser;

/**
 * 添加项审核(NapeUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 17:07:15
 */
public interface NapeUserService extends IService<NapeUser> {
    /**
     * 修改数据
     *
     * @param napeUser 实体对象
     * @return 修改结果
     */
    SaResult updateNapeUser(NapeUser napeUser);
}

