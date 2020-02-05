package com.geoxus.modules.wechat.open.service;

import com.geoxus.core.common.config.GXWeChatConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class WeChatOpenService extends WxOpenServiceImpl {
    @Autowired
    private GXWeChatConfig weChatConfig;

    @Autowired
    private RedisProperties redisProperties;

    private WxOpenMessageRouter wxOpenMessageRouter;

    @PostConstruct
    public void init() {
        final WxOpenInMemoryConfigStorage configStorage = new WxOpenInMemoryConfigStorage();
        configStorage.setComponentAppId(weChatConfig.getAppId());
        configStorage.setComponentAppSecret(weChatConfig.getAppSecret());
        configStorage.setComponentToken(weChatConfig.getToken());
        configStorage.setComponentAesKey(weChatConfig.getAesKey());
        setWxOpenConfigStorage(configStorage);
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
    }

    public WxOpenMessageRouter getWxOpenMessageRouter() {
        return wxOpenMessageRouter;
    }
}
