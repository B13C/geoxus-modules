package com.geoxus.modules.user.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SUserTokenConstants {
    @GXFieldCommentAnnotation(value = "核心模型ID")
    public static final int CORE_MODEL_ID = 200000;

    @GXFieldCommentAnnotation(value = "主键ID字段名")
    public static final String PRIMARY_KEY = "user_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_user_token";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "user_token";

    private SUserTokenConstants() {
    }
}
