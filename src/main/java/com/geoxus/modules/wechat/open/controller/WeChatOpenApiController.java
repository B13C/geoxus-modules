package com.geoxus.modules.wechat.open.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.modules.wechat.open.service.WeChatOpenService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wx-open/api")
public class WeChatOpenApiController {
    @Autowired
    private WeChatOpenService weChatOpenService;

    @PostMapping("/get-token-by-code")
    public String getTokenByCode(@RequestBody Dict dict) throws WxErrorException {
        final WxMpOAuth2AccessToken accessToken = weChatOpenService.getWxOpenComponentService().oauth2getAccessToken(dict.getStr("app_id"), dict.getStr("code"));
        return accessToken.getOpenId();
    }
}
