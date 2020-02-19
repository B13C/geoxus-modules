package com.geoxus.modules.order.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class OrderItemConstants {
    @GXFieldCommentAnnotation(zh = "主键ID名字")
    public static final String PRIMARY_KEY = "order_item_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "o_order_item";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "order_item";

    private OrderItemConstants() {
    }
}
