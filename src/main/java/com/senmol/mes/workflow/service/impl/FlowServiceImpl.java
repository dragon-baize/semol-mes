package com.senmol.mes.workflow.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.enums.TableEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.system.utils.SysFromRedis;
import com.senmol.mes.system.vo.MenuVo;
import com.senmol.mes.workflow.entity.Flow;
import com.senmol.mes.workflow.entity.FlowRecord;
import com.senmol.mes.workflow.entity.FlowUser;
import com.senmol.mes.workflow.mapper.FlowMapper;
import com.senmol.mes.workflow.service.FlowRecordService;
import com.senmol.mes.workflow.service.FlowService;
import com.senmol.mes.workflow.service.FlowUserService;
import com.senmol.mes.workflow.vo.MenuTrees;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批流程(Flow)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 14:25:48
 */
@Service("flowService")
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {

    @Resource
    private FlowUserService flowUserService;
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private RedisService redisService;
    @Resource
    private FlowRecordService flowRecordService;
    /**
     * 不需要审批的菜单目录
     */
    @Value("${flow.yes.approve}")
    private List<Long> menuIds;

    @Override
    public SaResult getByTitle(String title) {
        Flow flow = this.lambdaQuery().eq(Flow::getTitle, title).one();
        if (ObjectUtil.isNotNull(flow)) {
            flow.setCreateUserName(this.sysFromRedis.getUser(flow.getCreateUser()));

            // 获取流程审核员
            List<FlowUser> flowUsers = this.flowUserService.lambdaQuery()
                    .eq(FlowUser::getFlowId, flow.getId())
                    .orderByAsc(FlowUser::getSort)
                    .list();
            flow.setFlowUsers(flowUsers);
            return SaResult.data(flow);
        }

        return SaResult.ok();
    }

    @Override
    public List<MenuTrees> getList() {
        Object object = this.redisService.get(RedisKeyEnum.WORKFLOW_MENU_TREES.getKey());
        List<MenuTrees> trees = Convert.toList(MenuTrees.class, object);
        if (trees.size() > 0) {
            return trees;
        }

        List<MenuVo> menus = this.sysFromRedis.getMenus();
        for (MenuVo menu : menus) {
            if (menu.getPid() == 0L) {
                trees.add(findChildren(menu, menus));
            }
        }

        // 缓存菜单树
        this.redisService.set(RedisKeyEnum.WORKFLOW_MENU_TREES.getKey(), trees);
        return trees;
    }

    @Override
    public Flow selectOne(Long id) {
        Flow flow = this.getById(id);
        flow.setCreateUserName(this.sysFromRedis.getUser(flow.getCreateUser()));

        // 获取流程审核员
        List<FlowUser> flowUsers = this.flowUserService.lambdaQuery()
                .eq(FlowUser::getFlowId, id)
                .orderByAsc(FlowUser::getSort)
                .list();
        flow.setFlowUsers(flowUsers);
        return flow;
    }

    @Override
    public SaResult insertFlow(Flow flow) {
        List<Flow> list = this.lambdaQuery().eq(Flow::getTitle, flow.getTitle()).list();
        if (CollUtil.isNotEmpty(list)) {
            return SaResult.error("重复设置的审批流程");
        }

        List<FlowUser> flowUsers = flow.getFlowUsers();
        this.save(flow);
        // 保存审核员数据
        flowUsers.forEach(item -> item.setFlowId(flow.getId()));
        this.flowUserService.saveBatch(flowUsers);

        if (flow.getStatus() == 1) {
            this.redisService.hSet(RedisKeyEnum.WORKFLOW_TABLE.getKey(), flow.getTitle(), TableEnum.getSelf(flow.getTitle()));
        }
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateFlow(Flow flow) {
        // 校验是否存在待审核、审核中的数据
        List<FlowRecord> list = this.flowRecordService.lambdaQuery()
                .eq(FlowRecord::getFlowId, flow.getId())
                .lt(FlowRecord::getStatus, 2)
                .list();
        if (CollUtil.isNotEmpty(list)) {
            return SaResult.error("已开始流转的流程不允许修改");
        }

        List<FlowUser> flowUsers = flow.getFlowUsers();
        // 删除审核员数据
        this.flowUserService.lambdaUpdate().eq(FlowUser::getFlowId, flow.getId()).remove();
        // 保存审核员数据
        flowUsers.forEach(item -> item.setFlowId(flow.getId()));
        this.flowUserService.saveBatch(flowUsers);

        this.updateById(flow);

        if (flow.getStatus() == 1) {
            this.redisService.hSet(RedisKeyEnum.WORKFLOW_TABLE.getKey(), flow.getTitle(), TableEnum.getSelf(flow.getTitle()));
        } else {
            this.redisService.hDel(RedisKeyEnum.WORKFLOW_TABLE.getKey(), flow.getTitle());
        }
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteFlow(Long id) {
        Flow flow = this.getById(id);
        if (BeanUtil.isEmpty(flow)) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        if (flow.getIsUse() == 1) {
            return SaResult.error("已开始流转的流程不允许删除");
        }

        this.flowUserService.lambdaUpdate().eq(FlowUser::getFlowId, id).remove();
        this.removeById(id);
        this.redisService.hDel(RedisKeyEnum.WORKFLOW_TABLE.getKey(), flow.getTitle());
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }

    private MenuTrees findChildren(MenuVo parent, List<MenuVo> menus) {
        MenuTrees tree = Convert.convert(MenuTrees.class, parent);
        for (MenuVo child : menus) {
            if (!menuIds.contains(child.getId())) {
                continue;
            }

            if (tree.getId().equals(child.getPid())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }

                tree.getChildren().add(findChildren(child, menus));
            }
        }

        return tree;
    }

}

