package com.geoxus.modules.contents.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class FeedBackConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "feedback_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "p_feedback";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "feedback";

    @GXFieldCommentAnnotation(zh = "留言类型")
    public static final int LEAVE_WORD_TYPE = 1;

    @GXFieldCommentAnnotation(zh = "建议类型")
    public static final int SUGGEST_TYPE = 2;

    @GXFieldCommentAnnotation(zh = "其他类型")
    public static final int OTHER_TYPE = 10;

    private FeedBackConstants() {

    }
}
