package com.geoxus.modules.pay.ali.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class AliPayTradeConstants {
    @GXFieldCommentAnnotation(zh = "销售产品码，与支付宝签约的产品码名称。 注：目前仅支持QUICK_MSECURITY_PAY")
    public static final String ALI_PAY_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    private AliPayTradeConstants() {
    }
}
