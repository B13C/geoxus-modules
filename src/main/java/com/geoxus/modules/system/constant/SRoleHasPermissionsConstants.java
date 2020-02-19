package com.geoxus.modules.system.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SRoleHasPermissionsConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "s_role_has_permissions";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "role_has_permissions";

    private SRoleHasPermissionsConstants() {
    }
}
