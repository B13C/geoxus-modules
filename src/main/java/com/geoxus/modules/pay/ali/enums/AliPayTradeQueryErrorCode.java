package com.geoxus.modules.pay.ali.enums;

public enum AliPayTradeQueryErrorCode {
    SYSTEM_ERROR,
    INVALID_PARAMETER(500, "无效的参数"),
    TRADE_NOT_EXIST;

    private int status;

    private String message;

    AliPayTradeQueryErrorCode() {
    }

    AliPayTradeQueryErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
