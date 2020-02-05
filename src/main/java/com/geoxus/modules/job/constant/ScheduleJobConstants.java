package com.geoxus.modules.job.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class ScheduleJobConstants {
    @GXFieldCommentAnnotation(zh = "正常")
    public static final int NORMAL = 0;

    @GXFieldCommentAnnotation(zh = "暂停")
    public static final int PAUSE = 1;

    private ScheduleJobConstants() {
    }
}
