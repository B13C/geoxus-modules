package com.geoxus.modules.message.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class MessageConstants {
    @GXFieldCommentAnnotation(zh = "主键")
    public static final String PRIMARY_KEY = "message_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_message";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "message";
}
