package com.geoxus.modules.contents.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class CommentConstants {
    @GXFieldCommentAnnotation(zh = "主键ID名字")
    public static final String PRIMARY_KEY = "id";

    @GXFieldCommentAnnotation(zh = "表名")
    public static final String TABLE_NAME = "p_comment";

    private CommentConstants() {
    }
}
