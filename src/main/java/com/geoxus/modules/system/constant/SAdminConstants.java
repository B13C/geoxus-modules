package com.geoxus.modules.system.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXSAdminConstants;

public class SAdminConstants extends GXSAdminConstants {
    @GXFieldCommentAnnotation(zh = "主键")
    public static final String PRIMARY_KEY = "id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_admin";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "admin";

    private SAdminConstants() {
        super();
    }
}
