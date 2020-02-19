package com.geoxus.modules.goods.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class GoodsConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "goods_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "o_goods";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "goods";

    private GoodsConstants() {
    }
}
