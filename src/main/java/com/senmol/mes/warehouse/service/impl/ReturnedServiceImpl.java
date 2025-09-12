package com.senmol.mes.warehouse.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.common.utils.CheckToolUtil;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.warehouse.entity.ReturnedEntity;
import com.senmol.mes.warehouse.entity.StorageEntity;
import com.senmol.mes.warehouse.mapper.ReturnedMapper;
import com.senmol.mes.warehouse.service.ReturnedService;
import com.senmol.mes.warehouse.service.StorageService;
import com.senmol.mes.warehouse.vo.ReturnedVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 退库记录(Returned)表服务实现类
 *
 * @author makejava
 * @since 2023-08-04 20:14:40
 */
@Service("returnedService")
public class ReturnedServiceImpl extends ServiceImpl<ReturnedMapper, ReturnedEntity> implements ReturnedService {

    @Resource
    private StorageService storageService;
    @Resource
    private ProFromRedis proFromRedis;

    @Override
    public Page<ReturnedEntity> selectAll(Page<ReturnedEntity> page, ReturnedEntity returned) {
        return this.baseMapper.selectAll(page, returned);
    }

    @Override
    public SaResult byQrCode(String qrCode) {
        Long count = this.lambdaQuery()
                .eq(ReturnedEntity::getQrCode, qrCode)
                .last(CheckToolUtil.LIMIT)
                .count();
        if (count > 0L) {
            return SaResult.error("重复退库");
        }

        ReturnedVo returnedVo = this.baseMapper.byQrCode(qrCode);

        // 物料
        if (returnedVo.getType() > 1) {
            MaterialVo material = this.proFromRedis.getMaterial(returnedVo.getGoodsId());
            if (ObjUtil.isNotNull(material)) {
                returnedVo.setGoodsCode(material.getCode());
                returnedVo.setGoodsTitle(material.getTitle());
            }
        } else {
            ProductVo product = this.proFromRedis.getProduct(returnedVo.getGoodsId());
            if (ObjUtil.isNotNull(product)) {
                returnedVo.setGoodsCode(product.getCode());
                returnedVo.setGoodsTitle(product.getTitle());
            }
        }

        /*ReturnedVo vo = this.baseMapper.byObCode(returnedVo.getPickOrder(), returnedVo.getGoodsId());
        returnedVo.setOutputQty(vo.getOutputQty());
        returnedVo.setBaseQty(vo.getBaseQty());*/
        return SaResult.data(returnedVo);
    }

    @Override
    public SaResult insertReturnedList(List<ReturnedEntity> returnedList) {
        LocalDateTime now = LocalDateTime.now();
        long loginId = StpUtil.getLoginIdAsLong();
        returnedList.forEach(item -> {
            item.setCreateTime(now);
            item.setCreateUser(loginId);
        });

        this.saveBatch(returnedList);

        List<StorageEntity> list = new ArrayList<>(returnedList.size());
        for (ReturnedEntity returned : returnedList) {
            StorageEntity entity = new StorageEntity();
            entity.setBatchNo(returned.getInBatchNo());
            entity.setResidueQty(returned.getReturnQty());
            list.add(entity);
        }

        this.storageService.updateBatchByBatchNo(list);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

}

