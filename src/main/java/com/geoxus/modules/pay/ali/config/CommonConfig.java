package com.geoxus.modules.pay.ali.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CommonConfig {
    @Bean
    public AlipayClient alipayClient(@Autowired AliPayConfig aliPayConfig) {
        return new DefaultAlipayClient(
                aliPayConfig.getGatewayUrl(),
                aliPayConfig.getAppId(),
                aliPayConfig.getMerchantPrivateKey(), "json",
                aliPayConfig.getCharset(),
                aliPayConfig.getResAliPayPublicKey(), "RSA2");
    }
}
