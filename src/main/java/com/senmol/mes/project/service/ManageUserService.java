package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.ManageUser;

/**
 * 项目审核(ManageUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
public interface ManageUserService extends IService<ManageUser> {

    /**
     * 修改数据
     *
     * @param manageUser 实体对象
     * @return 修改结果
     */
    SaResult updateManageUser(ManageUser manageUser);
}

