package com.geoxus.modules.pay.ali.enums;

public enum AliPayTradeErrorCode {
    TRADE_TRANSFER_ERROR(3011, "转账到用户支付宝失败"),
    TRADE_PAY_ERROR(3012, "用户支付失败"),
    TRADE_QUERY_ERROR(3013, "支付查询失败");

    private int code;
    private String msg;

    AliPayTradeErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getId() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
