package com.geoxus.modules.pay.ali.vo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付宝退款信息
 */
@Data
@NoArgsConstructor
public class AliRefund implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付宝交易号(与商户号二选一)
     */
    private String tradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因 (示例值:正常退款)
     */
    private String refundReason;

    /**
     * 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     */
    private String outRequestNo;
}
