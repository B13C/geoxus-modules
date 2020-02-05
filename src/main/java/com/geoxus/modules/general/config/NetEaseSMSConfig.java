package com.geoxus.modules.general.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/netease-sms.yml"}, factory = GXYamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "netease-sms")
public class NetEaseSMSConfig {
    //网易云分配的账号
    private String secretId;

    //私钥
    private String secretKey;

    //验证码
    private String bussinessId;

    //通知类短信
    private String informUrl;

    //运营类短信
    private String opertingClass;

    //验证码长度
    private int codeLen;

    //当前版本号
    private String version;

    //短信接口地址
    private String apiUrl;

    //模板ID
    private String templateId;

    //短信参数(签名)
    private String params;
}
