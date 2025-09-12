package com.senmol.mes.produce.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.produce.entity.*;
import com.senmol.mes.produce.service.*;
import com.senmol.mes.produce.vo.*;
import com.senmol.mes.quality.entity.BadModeEntity;
import com.senmol.mes.quality.service.BadModeService;
import com.senmol.mes.quality.vo.BadModeVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class ProFromRedis {

    @Resource
    private RedisService redisService;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private ProductService productService;
    @Resource
    private StationService stationService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private WorkmanshipService workmanshipService;
    @Resource
    private MaterialService materialService;
    @Resource
    private ProcessService processService;
    @Resource
    private BomService bomService;
    @Resource
    private BomMaterialService bomMaterialService;
    @Resource
    private BadModeService badModeService;

    /**
     * 获取产线
     */
    public LineVo getLine(Long lineId) {
        if (ObjUtil.isNull(lineId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_LINE.getKey() + lineId);
        LineVo lineVo = Convert.convert(LineVo.class, object);

        if (BeanUtil.isEmpty(lineVo)) {
            ProductLineEntity productLine = this.productLineService.getById(lineId);
            if (ObjUtil.isNull(productLine)) {
                return null;
            }

            lineVo = Convert.convert(LineVo.class, productLine);
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_LINE.getKey() + lineId, lineVo,
                RedisKeyEnum.PRODUCE_LINE.getTimeout());

        return lineVo;
    }

    /**
     * 获取产线列表
     */
    public List<LineVo> getLineList() {
        List<LineVo> lineVos;

        Set<String> keys = this.redisService.keys(RedisKeyEnum.PRODUCE_LINE.getKey() + "*");
        if (CollUtil.isEmpty(keys)) {
            List<ProductLineEntity> list = this.productLineService.list();

            lineVos = new ArrayList<>(list.size());
            for (ProductLineEntity productLine : list) {
                LineVo lineVo = Convert.convert(LineVo.class, productLine);
                this.redisService.set(RedisKeyEnum.PRODUCE_LINE.getKey() + lineVo.getId(), lineVo,
                        RedisKeyEnum.PRODUCE_LINE.getTimeout());

                lineVos.add(lineVo);
            }
        } else {
            lineVos = new ArrayList<>(keys.size());
            for (String key : keys) {
                Object object = this.redisService.get(key);
                LineVo lineVo = Convert.convert(LineVo.class, object);
                lineVos.add(lineVo);

                this.redisService.set(RedisKeyEnum.PRODUCE_LINE.getKey() + lineVo.getId(), lineVo,
                        RedisKeyEnum.PRODUCE_LINE.getTimeout());
            }
        }

        return lineVos.stream()
                .sorted(Comparator.comparing(LineVo::getId))
                .collect(Collectors.toList());
    }

    /**
     * 获取产品
     */
    public ProductVo getProduct(Long productId) {
        if (ObjUtil.isNull(productId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_PRODUCT.getKey() + productId);
        if (ObjUtil.isNull(object)) {
            ProductEntity product = this.productService.getById(productId);
            if (ObjUtil.isNull(product)) {
                return null;
            }

            object = Convert.convert(ProductVo.class, product);
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_PRODUCT.getKey() + productId, object,
                RedisKeyEnum.PRODUCE_PRODUCT.getTimeout());

        return (ProductVo) object;
    }

    /**
     * 获取工位
     */
    public StationVo getStation(Long stationId) {
        if (ObjUtil.isNull(stationId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_STATION.getKey() + stationId);
        if (ObjUtil.isNull(object)) {
            StationEntity station = this.stationService.getById(stationId);
            if (ObjUtil.isNull(station)) {
                return null;
            }

            object = Convert.convert(StationVo.class, station);
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_STATION.getKey() + stationId, object,
                RedisKeyEnum.PRODUCE_STATION.getTimeout());

        return (StationVo) object;
    }

    /**
     * 获取工位列表
     */
    public List<StationVo> getStationList() {
        List<StationVo> stationVos;

        Set<String> keys = this.redisService.keys(RedisKeyEnum.PRODUCE_STATION.getKey() + "*");
        if (CollUtil.isEmpty(keys)) {
            List<StationEntity> list = this.stationService.list();

            stationVos = new ArrayList<>(list.size());
            for (StationEntity station : list) {
                StationVo stationVo = Convert.convert(StationVo.class, station);
                this.redisService.set(RedisKeyEnum.PRODUCE_STATION.getKey() + stationVo.getId(), stationVo,
                        RedisKeyEnum.PRODUCE_STATION.getTimeout());

                stationVos.add(stationVo);
            }
        } else {
            stationVos = new ArrayList<>(keys.size());
            for (String key : keys) {
                Object object = this.redisService.get(key);
                StationVo stationVo = Convert.convert(StationVo.class, object);
                stationVos.add(stationVo);

                this.redisService.set(RedisKeyEnum.PRODUCE_STATION.getKey() + stationVo.getId(), stationVo,
                        RedisKeyEnum.PRODUCE_STATION.getTimeout());
            }
        }

        return stationVos.stream()
                .sorted(Comparator.comparing(StationVo::getId))
                .collect(Collectors.toList());
    }

    /**
     * 获取设备
     */
    public DeviceVo getDevice(Long deviceId) {
        if (ObjUtil.isNull(deviceId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_DEVICE.getKey() + deviceId);
        if (ObjUtil.isNull(object)) {
            DeviceEntity device = this.deviceService.getById(deviceId);
            if (ObjUtil.isNull(device)) {
                return null;
            }

            object = Convert.convert(DeviceVo.class, device);
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_DEVICE.getKey() + deviceId, object,
                RedisKeyEnum.PRODUCE_DEVICE.getTimeout());
        return (DeviceVo) object;
    }

    /**
     * 获取工艺，工艺ID:v工艺版本号
     */
    public WorkmanshipVo getWorkmanship(String workmanship) {
        if (ObjUtil.isNull(workmanship)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + workmanship);
        if (ObjUtil.isNull(object)) {
            String[] strings = workmanship.split(":v");
            WorkmanshipVo vo = this.workmanshipService.getVoById(strings[0], strings[1]);
            if (ObjUtil.isNull(vo)) {
                return null;
            }

            String[] split = vo.getStrIds().split(",");
            List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
            vo.setProcessIds(collect);

            object = vo;
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_WORKMANSHIP.getKey() + workmanship, object,
                RedisKeyEnum.PRODUCE_WORKMANSHIP.getTimeout());
        return (WorkmanshipVo) object;
    }

    /**
     * 获取物料
     */
    public MaterialVo getMaterial(Long materialId) {
        if (ObjUtil.isNull(materialId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_MATERIAL.getKey() + materialId);
        if (ObjUtil.isNull(object)) {
            MaterialEntity material = this.materialService.getById(materialId);
            if (ObjUtil.isNull(material)) {
                return null;
            }

            object = Convert.convert(MaterialVo.class, material);
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_MATERIAL.getKey() + materialId, object,
                RedisKeyEnum.PRODUCE_MATERIAL.getTimeout());

        return (MaterialVo) object;
    }

    /**
     * 获取工序
     */
    public ProcessVo getProcess(Long processId) {
        if (ObjUtil.isNull(processId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_PROCESS.getKey() + processId);
        if (ObjUtil.isNull(object)) {
            object = this.processService.getByIdOrDel(processId);

            if (ObjUtil.isNull(object)) {
                return null;
            }
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_PROCESS.getKey() + processId, object,
                RedisKeyEnum.PRODUCE_PROCESS.getTimeout());
        return (ProcessVo) object;
    }

    /**
     * 获取清单
     */
    public BomVo getBom(Long productId) {
        if (ObjUtil.isNull(productId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.PRODUCE_BOM.getKey() + productId);
        if (ObjUtil.isNull(object)) {
            BomEntity bom = this.bomService.lambdaQuery().eq(BomEntity::getProductId, productId).one();
            if (ObjUtil.isNull(bom)) {
                return null;
            }

            List<BomMaterialEntity> bomMaterials = this.bomMaterialService.lambdaQuery().eq(BomMaterialEntity::getBomId,
                    bom.getId()).list();

            BomVo bomVo = Convert.convert(BomVo.class, bom);
            this.finished(bomVo, bomMaterials);

            object = bomVo;
        }

        this.redisService.set(RedisKeyEnum.PRODUCE_BOM.getKey() + productId, object,
                RedisKeyEnum.PRODUCE_BOM.getTimeout());
        return (BomVo) object;
    }

    /**
     * 获取不良模式
     */
    public BadModeVo getBadMode(Long badModeId) {
        if (ObjUtil.isNull(badModeId)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.QUALITY_BAD_MODE.getKey() + badModeId);
        if (ObjUtil.isNull(object)) {
            BadModeEntity badMode = this.badModeService.getById(badModeId);
            if (ObjUtil.isNull(badMode)) {
                return null;
            }

            object = Convert.convert(BadModeVo.class, badMode);
        }

        this.redisService.set(RedisKeyEnum.QUALITY_BAD_MODE.getKey() + badModeId, object,
                RedisKeyEnum.QUALITY_BAD_MODE.getTimeout());
        return (BadModeVo) object;
    }

    /**
     * 成品的清单
     */
    private void finished(BomVo bomVo, List<BomMaterialEntity> bomMaterials) {
        List<BomMaterialVo> materialVos = new ArrayList<>();

        WorkmanshipVo workmanshipVo = this.getWorkmanship(bomVo.getWorkmanshipId() + ":v" + bomVo.getWmsVersion());
        if (ObjUtil.isNull(workmanshipVo)) {
            materialVos = Convert.toList(BomMaterialVo.class, bomMaterials);
            bomVo.setMaterialVos(materialVos);
            return;
        }

        List<Long> processIds = workmanshipVo.getProcessIds();

        for (BomMaterialEntity bomMaterial : bomMaterials) {
            BomMaterialVo bomMaterialVo = Convert.convert(BomMaterialVo.class, bomMaterial);

            // 获取工序对应的工位、设备
            Long processId = bomMaterial.getProcessId();
            ProcessVo process = this.getProcess(processId);
            if (ObjUtil.isNotNull(process)) {
                Long stationId = process.getStationId();
                StationVo station = this.getStation(stationId);
                bomMaterialVo.setStationId(stationId);
                bomMaterialVo.setDeviceId(station.getDeviceId());
            }

            materialVos.add(bomMaterialVo);
            processIds.remove(processId);
        }

        // 工艺中为在清单中设置的工序也添加到清单的缓存中
        if (CollUtil.isNotEmpty(processIds)) {
            BomMaterialVo materialVo;
            for (Long processId : processIds) {
                materialVo = new BomMaterialVo();
                materialVo.setProcessId(processId);

                ProcessVo process = this.getProcess(processId);
                if (ObjUtil.isNotNull(process)) {
                    materialVo.setSerialNo(process.getSerialNo());

                    Long stationId = process.getStationId();
                    StationVo station = this.getStation(stationId);
                    if (ObjUtil.isNotNull(station)) {
                        materialVo.setStationId(stationId);
                        materialVo.setDeviceId(station.getDeviceId());
                    }
                }

                materialVo.setType(null);
                materialVos.add(materialVo);
            }
        }

        bomVo.setMaterialVos(materialVos);
    }

}
