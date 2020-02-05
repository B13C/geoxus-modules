package com.geoxus.modules.user.service.impl;

import com.geoxus.core.common.config.GXWeChatConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class WeChatServiceImpl extends WxMpServiceImpl {
    @Autowired
    private GXWeChatConfig weChatConfig;

    @Autowired
    private WxMpService wxMpService;

    @PostConstruct
    public void init() {
        final WxOpenInMemoryConfigStorage configStorage = new WxOpenInMemoryConfigStorage();
        configStorage.setComponentAppId(this.weChatConfig.getAppId());
        configStorage.setComponentAppSecret(this.weChatConfig.getAppSecret());
        configStorage.setComponentToken(this.weChatConfig.getToken());
        configStorage.setComponentAppSecret(this.weChatConfig.getAesKey());
        super.setWxMpConfigStorage(configStorage.getWxMpConfigStorage(weChatConfig.getAppId()));
    }

    /**
     * 获取微信openId
     *
     * @param code
     * @return
     */
    public WxMpOAuth2AccessToken wechatAccessToken(String code) {
        try {
            return wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
