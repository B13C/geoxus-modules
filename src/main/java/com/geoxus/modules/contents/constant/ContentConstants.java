package com.geoxus.modules.contents.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class ContentConstants {
    @GXFieldCommentAnnotation(zh = "模型标识")
    public static final String MODEL_IDENTIFICATION = "p_content";

    @GXFieldCommentAnnotation(zh = "表名")
    public static final String TABLE_NAME = "p_content";

    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "content_id";

    private ContentConstants() {
    }
}
