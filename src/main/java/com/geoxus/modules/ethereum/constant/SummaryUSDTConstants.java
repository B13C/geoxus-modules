package com.geoxus.modules.ethereum.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SummaryUSDTConstants {
    @GXFieldCommentAnnotation(zh = "缓存KEY")
    public static final String REDIS_LOCK_KEY = "gather.brt.gx.platform.key";

    @GXFieldCommentAnnotation(zh = "队列数据加密密钥")
    public static final String QUEUE_SECRET_KEY = "aTkfHayRRJpB6jwkU7BbL4bso";

    private SummaryUSDTConstants() {
    }
}
