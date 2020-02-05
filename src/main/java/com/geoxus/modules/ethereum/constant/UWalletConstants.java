package com.geoxus.modules.ethereum.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class UWalletConstants {
    @GXFieldCommentAnnotation(zh = "平台账户ID")
    public static final long PLATFORM_USER_ID = 100000;

    @GXFieldCommentAnnotation(zh = "加密password的密钥")
    public static final String META_PASSWORD = "brt.mac.network";

    @GXFieldCommentAnnotation(zh = "提币的队列名字")
    public static final String TRANSFER_QUEUE_NAME = "brt.transfer.queue";

    @GXFieldCommentAnnotation(zh = "KEY")
    public static final String TRANSFER_DATA_KEY = "brt.transfer.data.key";

    @GXFieldCommentAnnotation(zh = "核心模型ID")
    public static final int CORE_MODEL_ID = 100000;

    @GXFieldCommentAnnotation(zh = "转出Token的手续费(ETH的矿工费)")
    public static final double SERVICE_CHARGE = 0.005;

    private UWalletConstants() {

    }
}
