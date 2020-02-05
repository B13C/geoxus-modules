package com.geoxus.modules.pay.ali.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * 支付宝支付配置文件
 */
@Data
@Configuration(value = "aliPayConfig")
@ConfigurationProperties(prefix = "alipay", ignoreUnknownFields = false)
@PropertySource(value = "classpath:/ymls/${spring.profiles.active}/alipay.yml", factory = GXYamlPropertySourceFactory.class)
public class AliPayConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;

    private String pid;

    private String merchantPrivateKey;

    private String merchantPublicKey;

    private String resAliPayPublicKey;

    private String gatewayUrl;

    private String notifyUrl;

    private String signType;

    private String charset;

    private String subject;

    private String body;
}
