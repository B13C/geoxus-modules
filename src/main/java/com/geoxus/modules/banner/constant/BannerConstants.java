package com.geoxus.modules.banner.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class BannerConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "banner_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_banner";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "banner";

    @GXFieldCommentAnnotation(zh = "APP Banner")
    public static final int APP_TYPE = 1;

    @GXFieldCommentAnnotation(zh = "网站Banner")
    public static final int WEBSITE_TYPE = 2;

    @GXFieldCommentAnnotation(zh = "模型的值")
    public static final String MODEL_IDENTIFICATION_VALUE = "s_banner";

    private BannerConstants() {
    }
}
