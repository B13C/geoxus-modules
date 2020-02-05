package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.modules.general.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/generate/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get-graph-captcha")
    public GXResultUtils getGraphCaptcha() {
        Map<String, Object> map = captchaService.getCaptcha();
        return GXResultUtils.ok().putData(map);
    }

    @PostMapping("/get-sms-captcha")
    public GXResultUtils getSMSCaptcha(@RequestBody Dict dict) {
        final String phone = dict.getStr("phone");
        final String templateName = Optional.ofNullable(dict.getStr("template_name")).orElse("");
        GXSendSMSService sendSMSService = GXSpringContextUtils.getBean(GXSendSMSService.class);
        return sendSMSService.send(phone, templateName, Dict.create());
    }

    @PostMapping("/check-graph-captcha")
    public GXResultUtils checkGraphCaptcha(@RequestBody Dict param) {
        final boolean b = captchaService.checkCaptcha(param.getStr("uuid"), param.getStr("code"));
        if (b) {
            return GXResultUtils.ok().putData(Dict.create().set("msg", "图形验证码验证成功"));
        }
        return GXResultUtils.ok(1).putData(Dict.create().set("msg", "图形验证码验证失败"));
    }

    @PostMapping("/check-sms-captcha")
    public GXResultUtils checkSMSCaptcha(@RequestBody Dict param) {
        GXSendSMSService sendSMSService = GXSpringContextUtils.getBean(GXSendSMSService.class);
        final boolean b = sendSMSService.verification(param.getStr("phone"), param.getStr("code"));
        if (b) {
            return GXResultUtils.ok().putData(Dict.create().set("msg", "短信验证码验证成功"));
        }
        return GXResultUtils.ok(1).putData(Dict.create().set("msg", "短信验证码验证失败"));
    }
}
