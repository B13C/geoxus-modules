package com.geoxus.modules.banner.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class BannerConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "banner_id";

    @GXFieldCommentAnnotation(zh = "APP Banner")
    public static final int APP_TYPE = 1;

    @GXFieldCommentAnnotation(zh = "网站Banner")
    public static final int WEBSITE_TYPE = 2;

    private BannerConstants() {
    }
}
