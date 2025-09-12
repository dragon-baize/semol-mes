package com.senmol.mes.common.enums;

import cn.hutool.core.util.RandomUtil;

/**
 * 缓存key枚举，每次用到缓存数据时都会重新设置缓存时间
 *
 * @author Administrator
 */

public enum RedisKeyEnum {
    /**
     * 用户
     */
    SYS_USER("SYSTEM:SYS_USER:", 7 * 24 * 60 * 99L + RandomUtil.randomLong(1990)),
    /**
     * 用户ID-菜单ID关联
     */
    SYS_USER_MENUS("SYSTEM:SYS_USER_MENUS", -1L),
    /**
     * 部门树
     */
    SYS_DEPT_TREE("SYSTEM:DEPT_TREE", 3 * 24 * 60 * 88L + RandomUtil.randomLong(1880)),
    /**
     * 菜单
     */
    SYS_MENU("SYSTEM:SYS_MENU", -1),
    /**
     * 菜单树
     */
    SYS_MENU_TREE("SYSTEM:MENU_TREE", 3 * 24 * 60 * 20L + RandomUtil.randomLong(1200)),
    /**
     * 字典明细
     */
    SYS_DICT_MX("SYSTEM:SYS_DICT_MX:", 7 * 24 * 60 * 25L + RandomUtil.randomLong(1250)),
    /**
     * 部门
     */
    SYS_DEPT("SYSTEM:SYS_DEPT:", 7 * 24 * 60 * 48L + RandomUtil.randomLong(1480)),
    /**
     * 用于单位名称取单位ID
     */
    SYS_DICT_MX_ALL("SYSTEM:SYS_DICT_MX_ALL", 3 * 24 * 60 * 30L + RandomUtil.randomLong(1300)),
    /**
     * 权限列表
     */
    SYS_ALL_CLOSE_URI("PERMISSION_LIST:ALL_CLOSE_URI", -1L),
    /**
     * 权限树
     */
    SYS_PERMISSION_TREE("SYSTEM:PERMISSION_TREE", 7 * 24 * 60 * 35L + RandomUtil.randomLong(1350)),


    /**
     * 产线
     */
    PRODUCE_LINE("PRODUCE:PRODUCT_LINE:", 7 * 24 * 60 * 40L + RandomUtil.randomLong(1400)),
    /**
     * 产品
     */
    PRODUCE_PRODUCT("PRODUCE:PRODUCT:", 7 * 24 * 60 * 45L + RandomUtil.randomLong(1450)),
    /**
     * 物料
     */
    PRODUCE_MATERIAL("PRODUCE:MATERIAL:", 7 * 24 * 60 * 50L + RandomUtil.randomLong(1500)),
    /**
     * 用于物料导入
     */
    PRODUCE_MATERIAL_CODES("PRODUCE:MATERIAL_CODES", 3 * 24 * 60 * 55L + RandomUtil.randomLong(1550)),
    /**
     * 工位
     */
    PRODUCE_STATION("PRODUCE:STATION:", 7 * 24 * 60 * 65L + RandomUtil.randomLong(1650)),
    /**
     * 设备
     */
    PRODUCE_DEVICE("PRODUCE:DEVICE:", 7 * 24 * 60 * 70L + RandomUtil.randomLong(1700)),
    /**
     * 工艺
     */
    PRODUCE_WORKMANSHIP("PRODUCE:WORKMANSHIP:", 7 * 24 * 60 * 75L + RandomUtil.randomLong(1750)),
    /**
     * 工序
     */
    PRODUCE_PROCESS("PRODUCE:PROCESS:", 7 * 24 * 60 * 80L + RandomUtil.randomLong(1800)),
    /**
     * 清单
     */
    PRODUCE_BOM("PRODUCE:BOM:", 30 * 24 * 60 * 85L + RandomUtil.randomLong(1850)),
    /**
     * 不良模式
     */
    QUALITY_BAD_MODE("QUALITY:BAD_MODE:", 7 * 24 * 60 * 90L + RandomUtil.randomLong(1900)),


    /**
     * 客户
     */
    PLAN_CUSTOM("PLAN:CUSTOM:", 7 * 24 * 60 * 95L + RandomUtil.randomLong(1950)),

    /**
     * 供应商
     */
    PLAN_SUPPLIER("PLAN:SUPPLIER:", 7 * 24 * 60 * 28L + RandomUtil.randomLong(1280)),
    /**
     * 供应商物料
     */
    PLAN_MATERIAL("PLAN:CUSTOM:", 5 * 24 * 60 * 38L + RandomUtil.randomLong(1380)),

    /**
     * 库位
     */
    WAREHOUSE_STOCK("WAREHOUSE:STOCK:", 7 * 24 * 60 * 27L + RandomUtil.randomLong(1370)),
    /**
     * 生产计划ID
     */
    PLAN_PRODUCE_ID("PLAN_PRODUCE_ID:", -1L),
    /**
     * 获取物品基础信息
     */
    PRODUCE_GOODS("PRODUCE:GOODS", 3 * 60 * 48L + RandomUtil.randomLong(1480)),

    /**
     * 流程菜单树
     */
    WORKFLOW_MENU_TREES("WORKFLOW:MENU_TREES", -1L),
    /**
     * 流程表名
     */
    WORKFLOW_TABLE("WORKFLOW:TABLE", -1L);

    private final String key;

    private final long timeout;

    RedisKeyEnum(String key, long timeout) {
        this.key = key;
        this.timeout = timeout;
    }

    public String getKey() {
        return key;
    }

    public long getTimeout() {
        return timeout;
    }
}
