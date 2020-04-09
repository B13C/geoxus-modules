package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXCheckCaptchaAnnotation;
import com.geoxus.core.common.annotation.GXFrequencyLimitAnnotation;
import com.geoxus.core.common.service.GXCaptchaService;
import com.geoxus.core.common.service.GXEMailService;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/general/captcha")
public class CaptchaController {
    @Autowired
    private GXCaptchaService captchaService;

    @PostMapping("/get-graph-captcha")
    public GXResultUtils getGraphCaptcha(@RequestBody Dict param) {
        Map<String, Object> map = captchaService.getCaptcha(param);
        return GXResultUtils.ok().putData(map);
    }

    @PostMapping("/get-sms-captcha")
    @GXCheckCaptchaAnnotation()
    @GXFrequencyLimitAnnotation(key = "get-sms-captcha", count = 2, expire = 600)
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
            return GXResultUtils.ok().putData(Dict.create().set("status", 0));
        }
        return GXResultUtils.ok().putData(Dict.create().set("status", 1));
    }

    @PostMapping("/check-sms-captcha")
    public GXResultUtils checkSMSCaptcha(@RequestBody Dict param) {
        GXSendSMSService sendSMSService = GXSpringContextUtils.getBean(GXSendSMSService.class);
        final boolean b = sendSMSService.verification(param.getStr("phone"), param.getStr("code"));
        if (b) {
            return GXResultUtils.ok().putData(Dict.create().set("status", 0));
        }
        return GXResultUtils.ok().putData(Dict.create().set("status", 1));
    }

    @PostMapping("/get-email-captcha")
    public GXResultUtils getEmailCaptcha(@RequestBody Dict param) {
        final String email = param.getStr("email");
        final boolean b = GXSpringContextUtils.getBean(GXEMailService.class).sendVerifyCode(email);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/check-email-captcha")
    public GXResultUtils checkEmailCaptcha(@RequestBody Dict param) {
        final String email = param.getStr("email");
        final String code = param.getStr("code");
        final boolean b = GXSpringContextUtils.getBean(GXEMailService.class).verification(email, code);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
