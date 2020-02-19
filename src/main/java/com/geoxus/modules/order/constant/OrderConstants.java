package com.geoxus.modules.order.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class OrderConstants {
    @GXFieldCommentAnnotation(zh = "主键ID名字")
    public static final String PRIMARY_KEY = "order_sn";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "o_orders";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "orders";

    @GXFieldCommentAnnotation(zh = "订单核心模型ID")
    public static final int CORE_MODEL_ID = 40000;

    @GXFieldCommentAnnotation(zh = "实体商品订单类型")
    public static final int KIND_ORDER_TYPE = 1;

    @GXFieldCommentAnnotation(zh = "虚拟商品订单类型")
    public static final int VIRTUAL_ORDER_TYPE = 2;

    @GXFieldCommentAnnotation(zh = "团购订单类型")
    public static final int BULK_ORDER_TYPE = 4;

    @GXFieldCommentAnnotation(zh = "促销订单类型")
    public static final int SALES_ORDER_TYPE = 8;

    private OrderConstants() {
    }
}
