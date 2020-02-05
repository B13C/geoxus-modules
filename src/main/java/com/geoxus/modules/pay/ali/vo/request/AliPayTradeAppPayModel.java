package com.geoxus.modules.pay.ali.vo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付宝预创建订单需要信息
 */
@Data
@NoArgsConstructor
public class AliPayTradeAppPayModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 订单描述(可选)
     */
    private String body;

    /**
     * 商户订单号(必须唯一)
     */
    private String outTradeNo;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 支付总金额
     */
    private BigDecimal totalAmount;
}
