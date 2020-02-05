package com.geoxus.modules.pay.ali.enums;

public enum AliPayTradePayScene {
    BAR_CODE("bar_code"),
    WAVE_CODE("wave_code");

    private String value;

    AliPayTradePayScene(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
