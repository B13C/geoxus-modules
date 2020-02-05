package com.geoxus.modules.ethereum.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/eth.yml"}, factory = GXYamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "eth")
@ConditionalOnExpression(value = "${eth-enable:true}")
public class EthConfig {
    private String websocketClientUrl;

    private String httpClientUrl;

    private String contractAddress;

    private String targetDirectory;

    private String externalWalletAddress;
}
