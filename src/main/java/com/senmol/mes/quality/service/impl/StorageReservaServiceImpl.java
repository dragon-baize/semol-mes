package com.senmol.mes.quality.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senmol.mes.common.enums.ResultEnum;
import com.senmol.mes.plan.entity.CustomProductEntity;
import com.senmol.mes.plan.service.CustomProductService;
import com.senmol.mes.produce.utils.ProFromRedis;
import com.senmol.mes.produce.vo.MaterialVo;
import com.senmol.mes.produce.vo.ProductVo;
import com.senmol.mes.quality.entity.StorageInspectEntity;
import com.senmol.mes.quality.entity.StorageReserva;
import com.senmol.mes.quality.mapper.StorageReservaMapper;
import com.senmol.mes.quality.service.StorageInspectService;
import com.senmol.mes.quality.service.StorageReservaService;
import com.senmol.mes.quality.vo.StorageReservaVo;
import com.senmol.mes.system.utils.SysFromRedis;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 保留品记录(QualityStorageReserva)表服务实现类
 *
 * @author makejava
 * @since 2023-12-20 09:32:46
 */
@Service("qualityStorageReservaService")
public class StorageReservaServiceImpl extends ServiceImpl<StorageReservaMapper, StorageReserva> implements StorageReservaService {

    @Resource
    private SysFromRedis sysFromRedis;
    @Resource
    private StorageInspectService inspectService;
    @Resource
    private ProFromRedis proFromRedis;
    @Resource
    private CustomProductService customProductService;

    @Override
    public SaResult selectOne(Long id) {
        StorageReserva reserva = this.getById(id);
        StorageReservaVo reservaVo = Convert.convert(StorageReservaVo.class, reserva);
        if (ObjUtil.isNotNull(reservaVo)) {
            Long customId = reservaVo.getCustomId();
            if (ObjUtil.isNotNull(customId)) {
                CustomProductEntity customProduct = this.customProductService.lambdaQuery()
                        .eq(CustomProductEntity::getCustomId, customId)
                        .eq(CustomProductEntity::getProductId, reservaVo.getGoodsId())
                        .one();

                reservaVo.setCusProCode(customProduct.getCusProCode());
                reservaVo.setTaxRate(customProduct.getTaxRate());
            }

            if (reservaVo.getType() > 1) {
                MaterialVo material = this.proFromRedis.getMaterial(reservaVo.getGoodsId());
                if (ObjUtil.isNotNull(material)) {
                    reservaVo.setGoodsCode(material.getCode());
                    reservaVo.setGoodsTitle(material.getTitle());
                    reservaVo.setUnitTitle(this.sysFromRedis.getDictMx(material.getUnitId()));
                }
            } else {
                ProductVo product = this.proFromRedis.getProduct(reservaVo.getGoodsId());
                if (ObjUtil.isNotNull(product)) {
                    reservaVo.setGoodsCode(product.getCode());
                    reservaVo.setGoodsTitle(product.getTitle());
                    reservaVo.setUnitTitle(this.sysFromRedis.getDictMx(product.getUnitId()));
                }
            }
        }

        return SaResult.data(reservaVo);
    }

    @Override
    public Page<StorageReservaVo> selectAll(Page<StorageReservaVo> page, StorageReserva storageReserva) {
        List<StorageReservaVo> voList = this.baseMapper.selectAll(page, storageReserva);
        for (StorageReservaVo vo : voList) {
            // 字典
            vo.setUnitTitle(this.sysFromRedis.getDictMx(vo.getUnitId()));
            // 人员
            vo.setTesterName(this.sysFromRedis.getUser(vo.getTester()));
        }

        return page.setRecords(voList);
    }

    @Override
    public int getTodayCount(String date, Integer source) {
        return this.baseMapper.getTodayCount(date, source);
    }

    @Override
    public SaResult insertBatch(List<StorageReserva> reservas) {
        if (CollUtil.isEmpty(reservas)) {
            return SaResult.ok();
        }

        Integer returnType = reservas.get(0).getReturnType();
        long userId = StpUtil.getLoginIdAsLong();
        if (returnType == 1 || returnType == 2) {
            String date = LocalDate.now().toString();
            int count = this.baseMapper.getTodayCount(date, 2);
            String format = date.replace("-", "");

            for (int i = 0, j = reservas.size(); i < j; i++) {
                StorageReserva reserva = reservas.get(i);
                if (reserva.getReturnType() == 1) {
                    reserva.setCode("TH" + format + (101 + (count + i) * 3));
                    reserva.setDisposal(6);
                } else {
                    reserva.setCode("TJ" + format + (101 + (count + i) * 3));
                    reserva.setDisposal(7);
                    reserva.setInspectQty(reserva.getUnqualifiedQty());
                    reserva.setUnqualifiedQty(BigDecimal.ZERO);
                }

                reserva.setTester(userId);
            }
        } else {
            for (StorageReserva reserva : reservas) {
                reserva.setDetectionWay(1);
                reserva.setDisposal(2);
                reserva.setTester(userId);
            }
        }

        this.saveBatch(reservas);
        return SaResult.ok(ResultEnum.INSERT_SUCCESS.getMsg());
    }

    @Override
    public void saveReservaBatch(List<StorageReserva> reservas) {
        this.baseMapper.saveReservaBatch(reservas);
    }

    @Override
    public SaResult updateReserva(StorageReserva reserva) {
        StorageReserva storageReserva = this.getById(reserva.getId());
        if (reserva.getDisposal() > 3) {
            reserva.setStatus(1);
        }

        // 送检
        if (reserva.getDisposal() == 4) {
            String date = LocalDate.now().toString();
            int count = this.inspectService.getTodayCount(date);

            StorageInspectEntity inspect = new StorageInspectEntity();
            inspect.setId(storageReserva.getId());
            inspect.setCode("JC" + date.replace("-", "") + storageReserva.getType() + (101 + count * 3));
            inspect.setPid(storageReserva.getPid());
            inspect.setReceiptCode(storageReserva.getReceiptCode());
            inspect.setBatchNo(storageReserva.getBatchNo());
            inspect.setGoodsId(storageReserva.getGoodsId());
            inspect.setType(storageReserva.getType());
            inspect.setStatus(4);
            if (storageReserva.getDetectionWay() == 1 && storageReserva.getDisposal() == 1) {
                inspect.setCensorshipQty(storageReserva.getUnqualifiedQty());
            } else {
                inspect.setCensorshipQty(storageReserva.getCensorshipQty());
            }

            inspect.setSource(1);
            inspect.setTestResult(reserva.getTestResult());
            this.inspectService.save(inspect);
        }

        this.updateById(reserva);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public SaResult modifyBatch(List<StorageReserva> storageReservas) {
        this.updateBatchById(storageReservas);
        return SaResult.ok(ResultEnum.UPDATE_SUCCESS.getMsg());
    }

}

