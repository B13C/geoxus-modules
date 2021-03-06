package com.geoxus.modules.general.config;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/aliyun-sms.yml"}, factory = GXYamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "aliyun-sms")
public class AliYunSMSConfig {

    private String accessKey;

    private String accessKeySecret;

    private String signName;

    private Integer codeLen;

    private String endpoint;

    private Map<String, Dict> templates = new HashMap<>();
}
