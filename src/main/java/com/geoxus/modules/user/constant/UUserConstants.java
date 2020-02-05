package com.geoxus.modules.user.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class UUserConstants {
    @GXFieldCommentAnnotation(value = "核心模型ID")
    public static final int CORE_MODEL_ID = 100000;

    @GXFieldCommentAnnotation(value = "主键ID字段名")
    public static final String PRIMARY_KEY = "user_id";

    @GXFieldCommentAnnotation(value = "核心模型名字")
    public static final String MODEL_IDENTIFICATION = "u_user";

    @GXFieldCommentAnnotation(zh = "手机验证码验证")
    public static final int SMS_VERIFY = 1;

    @GXFieldCommentAnnotation(zh = "图形验证码验证")
    public static final int CAPTCHA_VERIFY = 2;

    private UUserConstants() {
    }
}
