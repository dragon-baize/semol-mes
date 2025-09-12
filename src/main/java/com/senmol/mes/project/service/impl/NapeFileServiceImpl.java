package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.project.entity.FileUser;
import com.senmol.mes.project.entity.NapeFile;
import com.senmol.mes.project.mapper.NapeFileMapper;
import com.senmol.mes.project.service.FileUserService;
import com.senmol.mes.project.service.NapeFileService;
import com.senmol.mes.project.vo.NapeFileInfo;
import com.senmol.mes.project.vo.NapeFileVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加项文件(NapeFile)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 09:43:40
 */
@Service("napeFileService")
public class NapeFileServiceImpl extends ServiceImpl<NapeFileMapper, NapeFile> implements NapeFileService {

    @Resource
    private FileUserService fileUserService;
    @Resource
    private SysFromRedis sysFromRedis;

    @Override
    public SaResult selectAll(Page<NapeFile> page, NapeFile napeFile) {
        this.page(page, new QueryWrapper<>(napeFile));
        List<NapeFile> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        }

        return SaResult.data(page);
    }

    @Override
    public SaResult getByUserId(Page<NapeFileVo> page, NapeFile napeFile) {
        List<NapeFileVo> list = this.baseMapper.getByUserId(page, napeFile, StpUtil.getLoginIdAsLong());
        list.forEach(item -> item.setCreateUserName(this.sysFromRedis.getUser(item.getCreateUser())));
        page.setRecords(list);
        return SaResult.data(page);
    }

    @Override
    public SaResult getOneById(Long id) {
        NapeFileInfo info = this.baseMapper.getOneById(id);
        // 人员ID转换
        info.setCreateUserName(this.sysFromRedis.getUser(info.getCreateUser()));
        // 审批信息列表字符串转换
        List<FileUser> fileUsers = new ArrayList<>();
        String userApprove = info.getUserApprove();
        String[] split = userApprove.split(",");
        for (String string : split) {
            String[] s = string.split("_");
            FileUser fileUser = new FileUser();
            fileUser.setUserId(Long.parseLong(s[0]));
            fileUser.setUserName(this.sysFromRedis.getUser(Long.parseLong(s[0])));
            fileUser.setIsApprove(Integer.parseInt(s[1]));
            if (s.length > 2) {
                fileUser.setRemarks(s[2]);
            }

            fileUsers.add(fileUser);
        }

        info.setFileUsers(fileUsers);
        return SaResult.data(info);
    }

    @Override
    public SaResult insertNapeFile(NapeFile napeFile) {
        List<Long> userIds = napeFile.getUserIds();
        this.save(napeFile);

        if (ObjectUtil.isNotNull(napeFile.getId()) && CollUtil.isNotEmpty(userIds)) {
            List<FileUser> fileUsers = new ArrayList<>(userIds.size());
            for (Long userId : userIds) {
                FileUser fileUser = new FileUser();
                fileUser.setFileId(napeFile.getId());
                fileUser.setUserId(userId);
                fileUsers.add(fileUser);
            }

            this.fileUserService.saveBatch(fileUsers);
        }

        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult deleteNapeFile(List<Long> idList) {
        List<NapeFile> list = this.lambdaQuery().in(NapeFile::getId, idList).list();
        long count = list.stream().filter(item -> item.getApproveResult() == 1).count();
        if (count > 0L) {
            return SaResult.error("审核通过的文件不允许删除");
        }

        this.fileUserService.lambdaUpdate().in(FileUser::getFileId, idList).remove();
        this.removeByIds(idList);
        return SaResult.ok(ResultEnum.DELETE_SUCCESS.getMsg());
    }
}

