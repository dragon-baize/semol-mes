package com.senmol.mes.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.vo.PageUser;

/**
 * 用户(User)表服务接口
 *
 * @author makejava
 * @since 2022-11-22 13:32:04
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    SaResult selectAll(Page<PageUser> page, UserEntity user);

    /**
     * 通过主键查询单条数据(包括用户绑定的角色、菜单)
     *
     * @param id 主键
     * @return 单条数据
     */
    UserEntity selectOne(Long id);

    /**
     * 查询用户数据包括删除的
     *
     * @param id 用户ID
     * @return 用户数据
     */
    UserEntity getByIdOrDel(Long id);

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    SaResult insertUser(UserEntity user);

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    SaResult updateUser(UserEntity user);

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    SaResult deleteUser(Long id);

}

