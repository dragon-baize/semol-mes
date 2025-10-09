package com.senmol.mes.warehouse.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.senmol.mes.common.enums.RedisKeyEnum;
import com.senmol.mes.common.redis.RedisService;
import com.senmol.mes.warehouse.entity.StockEntity;
import com.senmol.mes.warehouse.service.StockService;
import com.senmol.mes.warehouse.vo.StockVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Component
public class WarAsyncUtil {

    @Resource
    private StockService stockService;
    @Resource
    private RedisService redisService;

    /**
     * 获取库位缓存
     */
    public StockVo getStock(Long id) {
        if (ObjUtil.isNull(id)) {
            return null;
        }

        Object object = this.redisService.get(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + id);
        if (BeanUtil.isEmpty(object)) {
            StockEntity stock = this.stockService.getById(id);
            if (ObjUtil.isNull(stock)) {
                return null;
            }

            object = Convert.convert(StockVo.class, stock);
        }

        this.redisService.set(RedisKeyEnum.WAREHOUSE_STOCK.getKey() + id, object, RedisKeyEnum.WAREHOUSE_STOCK.getTimeout());

        return (StockVo) object;
    }

}
