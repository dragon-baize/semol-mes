package com.senmol.mes.common.enums;

/**
 * @author Administrator
 */

public enum TableEnum {
    /**
     * 表中文-英文
     */
    plan_outbound("出库单"),

    plan_outsource("委外计划"),

    plan_produce("生产计划"),

    plan_requisition("请购单"),

    plan_sale_order("销售/备货订单"),

    produce_bom("物料清单"),

    produce_workmanship("生产工艺");

    private String name;

    TableEnum(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getSelf(String name) {
        for (TableEnum value : values()) {
            if (value.name.equals(name)) {
                return value.toString();
            }
        }

        return null;
    }
}
