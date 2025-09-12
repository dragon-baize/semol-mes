package com.senmol.mes.quality.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.mapper.StorageInspectMapper;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.quality.vo.StorageInspectVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 入库检测(StorageInspect)表服务实现类
 *
 * @author makejava
 * @since 2023-01-31 09:45:22
 */
@Service("storageInspectService")
public class StorageInspectServiceImpl extends ServiceImpl<StorageInspectMapper, StorageInspectEntity> implements StorageInspectService {

    @Lazy
    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StorageReservaService storageReservaService;

    @Override
    public Page<StorageInspectVo> selectAll(Page<StorageInspectVo> page, StorageInspectEntity storageInspect) {
        List<StorageInspectVo> voList = this.baseMapper.selectAll(page, storageInspect);
        for (StorageInspectVo vo : voList) {
            // 字典
            vo.setUnitTitle(this.sysFromRedis.getDictMx(vo.getUnitId()));
            // 人员
            vo.setTesterName(this.sysFromRedis.getUser(vo.getTester()));
        }

        return page.setRecords(voList);
    }

    @Override
    public SaResult insertStorageInspect(StorageInspectEntity storageInspect) {
        if (storageInspect.getSource() == 1) {
            BigDecimal zero = new BigDecimal(0);
            if (storageInspect.getQualifiedQty().equals(zero) && storageInspect.getUnqualifiedQty().equals(zero)) {
                return SaResult.error("合格数量、不合格数量不能都为0");
            }
        }

        if (ObjectUtil.isNull(storageInspect.getCode())) {
            Date date = new Date();
            Long count = this.lambdaQuery().between(StorageInspectEntity::getCreateTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)).count();
            storageInspect.setCode("JC" + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN) + storageInspect.getType() + (101 + count * 3));
        } else {
            storageInspect.setTester(StpUtil.getLoginIdAsLong());
            storageInspect.setStatus(1);
        }

        if (ObjectUtil.isNotNull(storageInspect.getUnqualifiedQty()) && storageInspect.getUnqualifiedQty().equals(new BigDecimal(0))) {
            storageInspect.setDisposal(0);
        }

        if (ObjectUtil.isNotNull(storageInspect.getDisposal()) && storageInspect.getDisposal() != 0) {
            // 部分入库处理、入保留品库处理
            StorageReserva storageReserva = Convert.convert(StorageReserva.class, storageInspect);
            storageReserva.setId(null);
            storageReserva.setStatus(0);
            storageReserva.setSource(0);

            this.storageReservaService.save(storageReserva);
        }

        this.save(storageInspect);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public SaResult updateStorageInspect(StorageInspectEntity storageInspect) {
        BigDecimal zero = new BigDecimal(0);
        if (storageInspect.getQualifiedQty().equals(zero) && storageInspect.getUnqualifiedQty().equals(zero)) {
            return SaResult.error("合格数量、不合格数量不能都为0");
        }

        if (!storageInspect.getQualifiedQty().equals(storageInspect.getInspectQty()) && storageInspect.getDisposal() == 0) {
            return SaResult.error("合格数量不等于验货数量，处理方式不匹配");
        }

        if (!storageInspect.getUnqualifiedQty().equals(BigDecimal.ZERO) && storageInspect.getDisposal() == 0) {
            return SaResult.error("不合格数量大于0，处理方式不匹配");
        }

        if (storageInspect.getQualifiedQty().equals(BigDecimal.ZERO) && storageInspect.getDisposal() == 1) {
            return SaResult.error("合格数量为0，处理方式不匹配");
        }

        if (storageInspect.getUnqualifiedQty().equals(BigDecimal.ZERO)) {
            storageInspect.setDisposal(0);
        }

        if (storageInspect.getDisposal() != 0) {
            // 部分入库处理、入保留品库处理
            StorageReserva storageReserva = Convert.convert(StorageReserva.class, storageInspect);
            storageReserva.setId(null);
            storageReserva.setStatus(0);
            storageReserva.setSource(0);

            this.storageReservaService.save(storageReserva);

            if (storageInspect.getDisposal() == 2) {
                this.removeById(storageInspect.getId());
            }
        }

        this.updateById(storageInspect);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SaResult back(Long id) {
        this.lambdaUpdate()
                .set(StorageInspectEntity::getStatus, 3)
                .eq(StorageInspectEntity::getId, id)
                .update();
        return SaResult.ok("退回成功");
    }

}

