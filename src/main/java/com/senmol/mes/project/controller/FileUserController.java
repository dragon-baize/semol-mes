package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.FileUser;
import com.senmol.mes.project.service.FileUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文件审核(FileUser)表控制层
 *
 * @author makejava
 * @since 2023-03-21 16:19:06
 */
@RestController
@RequestMapping("/project/fileUser")
public class FileUserController {

    @Resource
    private FileUserService fileUserService;

    /**
     * 修改数据
     *
     * @param fileUser 实体对象
     * @return 修改结果
     */
    @Logger("文件审核")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody FileUser fileUser) {
        return this.fileUserService.updateFileUser(fileUser);
    }

}

