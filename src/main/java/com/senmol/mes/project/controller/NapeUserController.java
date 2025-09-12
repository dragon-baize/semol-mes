package com.senmol.mes.project.controller;

import cn.dev33.satoken.util.SaResult;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.project.entity.NapeUser;
import com.senmol.mes.project.service.NapeUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 添加项审核(NapeUser)表控制层
 *
 * @author makejava
 * @since 2023-03-21 17:07:15
 */
@Validated
@RestController
@RequestMapping("/project/napeUser")
public class NapeUserController {

    @Resource
    private NapeUserService napeUserService;

    /**
     * 修改数据
     *
     * @param napeUser 实体对象
     * @return 修改结果
     */
    @Logger("添加项审核")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody NapeUser napeUser) {
        return this.napeUserService.updateNapeUser(napeUser);
    }
}

