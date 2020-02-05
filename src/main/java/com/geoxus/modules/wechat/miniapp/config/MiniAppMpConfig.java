package com.geoxus.modules.wechat.miniapp.config;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaMsgServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaQrcodeServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaUserServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiniAppMpConfig {
    @Autowired
    private MiniAppAccountConfig accountConfig;

    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaConfig());
        return wxMaService;
    }

    @Bean
    public WxMaConfig wxMaConfig() {
        final WxOpenInMemoryConfigStorage configStorage = new WxOpenInMemoryConfigStorage();
        configStorage.setComponentAppId(accountConfig.getMiniAppId());
        configStorage.setComponentAppSecret(accountConfig.getMiniSecret());
        configStorage.setComponentToken(accountConfig.getMiniToken());
        return configStorage.getWxMaConfig(accountConfig.getMiniAppId());
    }

    @Bean
    public WxMaQrcodeService wxMaQrcodeService(WxMaService wxMaService) {
        WxMaQrcodeService wxMaQrcodeService = new WxMaQrcodeServiceImpl(wxMaService);
        return wxMaQrcodeService;
    }

    @Bean
    public WxMaMsgService wxMaMsgService(WxMaService wxMaService) {
        WxMaMsgService wxMaMsgService = new WxMaMsgServiceImpl(wxMaService);
        return wxMaMsgService;
    }

    @Bean
    public WxMaUserService wxMaUserService(WxMaService wxMaService) {
        WxMaUserService wxMaUserService = new WxMaUserServiceImpl(wxMaService);
        return wxMaUserService;
    }

    @Bean
    public WxMaMessageRouter wxMaMessageRouter(WxMaService wxMaService) {
        WxMaMessageRouter wxMaMessageRouter = new WxMaMessageRouter(wxMaService);
        return wxMaMessageRouter;
    }
}
