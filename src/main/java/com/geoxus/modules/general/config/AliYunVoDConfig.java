package com.geoxus.modules.general.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun-vod")
@PropertySource(value = "classpath:/ymls/${spring.profiles.active}/aliyun-vod.yml", factory = GXYamlPropertySourceFactory.class)
public class AliYunVoDConfig {

	private String accessKey;
	
	private String accessKeySecret;
	
	private String templateGroupId;
}
