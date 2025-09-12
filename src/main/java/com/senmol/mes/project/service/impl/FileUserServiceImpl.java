package com.senmol.mes.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.project.entity.FileUser;
import com.senmol.mes.project.entity.NapeFile;
import com.senmol.mes.project.mapper.FileUserMapper;
import com.senmol.mes.project.service.FileUserService;
import com.senmol.mes.project.service.NapeFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件审核(FileUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 13:37:06
 */
@Service("fileUserService")
public class FileUserServiceImpl extends ServiceImpl<FileUserMapper, FileUser> implements FileUserService {

    @Resource
    private NapeFileService napeFileService;

    @Override
    public SaResult updateFileUser(FileUser fileUser) {
        List<FileUser> list = this.lambdaQuery().eq(FileUser::getFileId, fileUser.getFileId()).list();
        if (list.size() < 1) {
            return SaResult.error(ResultEnum.DATA_NOT_EXIST.getMsg());
        }

        long count = list.stream().filter(item -> item.getIsApprove() == 2).count();
        if (count > 0L) {
            return SaResult.error("审核不通过的数据无需再审");
        }

        count = list.stream().filter(item -> item.getIsApprove() == 1).count();
        if (count > 0L) {
            return SaResult.error("审核通过的数据无需再审");
        }

        // 先完成数据记录
        this.lambdaUpdate()
                .eq(FileUser::getFileId, fileUser.getFileId())
                .eq(FileUser::getUserId, StpUtil.getLoginIdAsLong())
                .update(fileUser);

        // 本次审核为不通过
        if (fileUser.getIsApprove() == 2) {
            // 终止所有审核
            this.napeFileService.lambdaUpdate()
                    .set(NapeFile::getApproveResult, 2)
                    .eq(NapeFile::getId, fileUser.getFileId())
                    .update();
        } else if (fileUser.getIsApprove() == 1) {
            // 校验是否所有审核员都审核完成
            List<FileUser> fileUsers =
                    list.stream().filter(item -> item.getIsApprove() == 0).collect(Collectors.toList());
            FileUser one = fileUsers.get(0);
            // 其他人都审完，本次审核人也审核通过，整个流程审核通过
            if (fileUsers.size() == 1 && one.getUserId().equals(fileUser.getUserId())) {
                // 文件通过审核
                this.napeFileService.lambdaUpdate()
                        .set(NapeFile::getApproveResult, 1)
                        .eq(NapeFile::getId, fileUser.getFileId())
                        .update();
            }
        }

        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }
}

