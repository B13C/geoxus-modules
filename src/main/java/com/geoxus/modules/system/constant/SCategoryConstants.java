package com.geoxus.modules.system.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SCategoryConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "category_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_category";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "category";

    @GXFieldCommentAnnotation(zh = "模型标识")
    public static final String MODEL_IDENTIFICATION = "s_category";

    private SCategoryConstants() {
    }
}
