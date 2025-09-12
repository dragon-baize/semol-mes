package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.ManageUser;
import com.senmol.mes.project.service.ManageUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 项目审核(ManageUser)表控制层
 *
 * @author makejava
 * @since 2023-03-22 09:27:15
 */
@Validated
@RestController
@RequestMapping("/project/manageUser")
public class ManageUserController {

    @Resource
    private ManageUserService manageUserService;

    /**
     * 项目审批
     *
     * @param manageUser 实体对象
     * @return 修改结果
     */
    @Logger("项目审核")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody ManageUser manageUser) {
        return this.manageUserService.updateManageUser(manageUser);
    }

}

