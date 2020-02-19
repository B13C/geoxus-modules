package com.geoxus.modules.general.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SlogConstants {
    @GXFieldCommentAnnotation(zh = "主键ID名字")
    public static final String PRIMARY_KEY = "log_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_log";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "log";

    private SlogConstants() {
    }
}
