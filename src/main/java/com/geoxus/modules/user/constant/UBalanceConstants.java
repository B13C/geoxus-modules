package com.geoxus.modules.user.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class UBalanceConstants {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "balance_id";

    @GXFieldCommentAnnotation(zh = "数据表名")
    public static final String TABLE_NAME = "u_balance";

    @GXFieldCommentAnnotation(zh = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "balance";

    @GXFieldCommentAnnotation(zh = "余额核心模型")
    public static final int CORE_MODEL_ID = 200000;

    @GXFieldCommentAnnotation(zh = "平台余额")
    public static final int PLATFORM_BALANCE_TYPE = 0x0;

    @GXFieldCommentAnnotation(zh = "可用余额")
    public static final int AVAILABLE_BALANCE_TYPE = 0x10;

    @GXFieldCommentAnnotation(zh = "冻结余额")
    public static final int FROZEN_BALANCE_TYPE = 0x20;

    @GXFieldCommentAnnotation(zh = "可用积分")
    public static final int AVAILABLE_INTEGRAL_TYPE = 0x30;

    @GXFieldCommentAnnotation(zh = "冻结积分")
    public static final int FROZEN_INTEGRAL_TYPE = 0x40;

    @GXFieldCommentAnnotation(zh = "可用USDT")
    public static final int AVAILABLE_USDT_TYPE = 0x50;

    @GXFieldCommentAnnotation(zh = "冻结USDT")
    public static final int FROZEN_USDT_TYPE = 0x60;

    @GXFieldCommentAnnotation(zh = "可用ETH")
    public static final int AVAILABLE_ETH_TYPE = 0x70;

    @GXFieldCommentAnnotation(zh = "冻结ETH")
    public static final int FROZEN_ETH_TYPE = 0x80;

    private UBalanceConstants() {
    }
}
