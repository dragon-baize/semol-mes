package com.senmol.mes.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senmol.mes.system.entity.UserEntity;
import com.senmol.mes.system.vo.PageUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-22 13:30:54
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    List<PageUser> selectAll(Page<PageUser> page, @Param("user") UserEntity user);

    /**
     * 查询用户数据包括删除的
     *
     * @param id 用户ID
     * @return 用户数据
     */
    UserEntity getByIdOrDel(@Param("id") Long id);

}

