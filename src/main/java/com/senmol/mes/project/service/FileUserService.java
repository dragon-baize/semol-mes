package com.senmol.mes.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.project.entity.FileUser;

/**
 * 文件审核(FileUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 13:37:06
 */
public interface FileUserService extends IService<FileUser> {
    /**
     * 修改数据
     *
     * @param fileUser 实体对象
     * @return 修改结果
     */
    SaResult updateFileUser(FileUser fileUser);
}

