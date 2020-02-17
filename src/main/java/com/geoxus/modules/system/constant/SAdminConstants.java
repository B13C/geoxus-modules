package com.geoxus.modules.system.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXSAdminConstants;

public class SAdminConstants extends GXSAdminConstants {
    @GXFieldCommentAnnotation(zh = "主键")
    public static final String PRIMARY_KEY = "id";

    private SAdminConstants() {
        super();
    }
}
