package com.senmol.mes.system.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.common.utils.ParamsValidate;
import com.senmol.mes.log.annotation.Logger;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.service.UserService;
import com.senmol.mes.system.vo.UserInfo;
import com.senmol.mes.system.vo.PageUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户(User)表控制层
 *
 * @author makejava
 * @since 2022-11-22 13:33:03
 */
@Validated
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("getList")
    public SaResult getList(UserEntity user) {
        user.setStatus(1);
        user.setOnJob(1);

        LambdaQueryChainWrapper<UserEntity> admin = this.userService.lambdaQuery(user);
        if (ObjectUtil.isNotNull(user.getType()) && user.getType() == 0) {
            admin.ne(UserEntity::getType, 1);
        } else if (ObjectUtil.isNotNull(user.getType()) && user.getType() == 1) {
            admin.ne(UserEntity::getType, 0);
        }

        List<UserInfo> infos = Convert.toList(UserInfo.class, admin.list());
        return SaResult.data(infos);
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @GetMapping
    public SaResult selectAll(Page<PageUser> page, UserEntity user) {
        return this.userService.selectAll(page, user);
    }

    /**
     * 通过主键查询单条数据(包括用户绑定的角色、菜单)
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public SaResult selectOne(@PathVariable Long id) {
        return SaResult.data(this.userService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @Logger("用户新增")
    @PostMapping
    public SaResult insert(@Validated(ParamsValidate.Insert.class) @RequestBody UserEntity user) {
        return this.userService.insertUser(user);
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @Logger("用户修改")
    @PutMapping
    public SaResult update(@Validated(ParamsValidate.Update.class) @RequestBody UserEntity user) {
        return this.userService.updateUser(user);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @Logger("用户删除")
    @DeleteMapping("{id}")
    public SaResult delete(@PathVariable("id") Long id) {
        return this.userService.deleteUser(id);
    }

}

