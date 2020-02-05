package com.geoxus.modules.wechat.miniapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wx.mini.configs")
public class MiniAppAccountConfig {
    /**
     * 小程序APP_ID
     */
    private String miniAppId;

    /**
     * 小程序Token
     */
    private String miniToken;

    /**
     * 小程序 Secret
     */
    private String miniSecret;
}
