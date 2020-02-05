package com.geoxus.modules.pay.ali.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class AliPayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int status;

    private Integer code;

    public AliPayException(String message) {
        super(message);
        this.code = 0;
        this.status = 500;
    }

    public AliPayException(int status) {
        this.status = status;
    }

    public AliPayException(int errorCode, String message) {
        this(400, errorCode, message);
    }

    public AliPayException(int status, Integer code, String message) {
        this(status, code, message, null);
    }

    public AliPayException(int status, Integer code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return this.status;
    }

    public Integer getCode() {
        return this.code;
    }

    public void logException(Logger logger) {
        if (this.status >= 500) {
            logger.error(this.getMessage(), this);
        } else if (StringUtils.isNotEmpty(this.getMessage())) {
            logger.warn(this.getMessage());
        }
    }
}
