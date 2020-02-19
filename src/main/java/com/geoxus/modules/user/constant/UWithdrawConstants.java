package com.geoxus.modules.user.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class UWithdrawConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "withdraw_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "u_withdraw";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "withdraw";

    private UWithdrawConstants() {
    }
}
