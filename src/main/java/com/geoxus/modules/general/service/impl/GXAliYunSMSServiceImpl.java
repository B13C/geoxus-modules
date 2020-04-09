package com.geoxus.modules.general.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXCacheKeysUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.modules.general.config.AliYunSMSConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnExpression("'${sms-provider}'.equals('aliyun-sms')")
public class GXAliYunSMSServiceImpl implements GXSendSMSService {
    @GXFieldCommentAnnotation(zh = "Guava缓存组件")
    private static final Cache<String, String> ALI_YUN_SMS_CACHE;

    static {
        ALI_YUN_SMS_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(Duration.ofSeconds(300L)).build();
    }

    @Autowired
    private GXCacheKeysUtils gxCacheKeysUtils;

    @Autowired
    private AliYunSMSConfig aliYunSMSConfig;

    @Override
    public GXResultUtils send(String phone, String templateName, Dict param) {
        String cacheKey = gxCacheKeysUtils.getAliYunSMSCodeConfigKey(phone);
        if (ALI_YUN_SMS_CACHE.getIfPresent(cacheKey) != null) {
            throw new GXException("操作频繁,请稍后再试....");
        }
        final Dict aliYunTemplateConfig = aliYunSMSConfig.getTemplates().get(templateName);
        if (null == aliYunTemplateConfig) {
            throw new GXException("阿里云短信模板不存在....");
        }
        String codeName = "code";
        Integer codeLen = Optional.ofNullable(aliYunTemplateConfig.getInt("codeLen"))
                .orElse(Optional.ofNullable(aliYunSMSConfig.getCodeLen()).orElse(6));
        String code = Optional.ofNullable(param.getStr(codeName))
                .orElse(RandomStringUtils.randomNumeric(codeLen));
        param.put(codeName, code);
        String templateParam = JSONUtil.toJsonStr(param);
        final boolean sendResult = this.processSend(phone, aliYunTemplateConfig, templateParam);
        if (sendResult) {
            storeCode(phone, code);
            return GXResultUtils.ok();
        }
        return GXResultUtils.ok(GXResultCode.SMS_SEND_FAILURE);
    }

    @Override
    public boolean verification(String phone, String code) {
        if (StrUtil.isEmpty(phone) || StrUtil.isEmpty(code)) {
            return false;
        }
        String key = gxCacheKeysUtils.getAliYunSMSCodeConfigKey(phone);
        final String s = ALI_YUN_SMS_CACHE.getIfPresent(key);
        if (code.equalsIgnoreCase(s)) {
            ALI_YUN_SMS_CACHE.invalidate(key);
            return true;
        }
        return false;
    }

    /**
     * 处理实际发送
     *
     * @param phone         手机号码
     * @param templateData  短信模板的信息
     * @param templateParam 模板参数值
     * @return boolean
     */
    private boolean processSend(String phone, Dict templateData, String templateParam) {
        String templateCode = templateData.getStr("templateId");
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        String regionId = Optional.ofNullable(templateData.getStr("endPoint")).orElse(aliYunSMSConfig.getEndpoint());
        String accessKey = Optional.ofNullable(templateData.getStr("accessKey")).orElse(aliYunSMSConfig.getAccessKey());
        String accessKeySecret = Optional.ofNullable(templateData.getStr("accessKeySecret")).orElse(aliYunSMSConfig.getAccessKeySecret());
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessKeySecret);
        DefaultProfile.addEndpoint(regionId, "Dysmsapi", "dysmsapi.aliyuncs.com");
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        String signName = Optional.ofNullable(templateData.getStr("signName")).orElse(aliYunSMSConfig.getSignName());
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到SMS_157070109/SMS_156730092
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        try {
            SendSmsResponse response = acsClient.getAcsResponse(request);
            if (!response.getCode().equals("OK")) {
                log.warn(StrUtil.format("阿里云短信发送失败, 原因 : {}, 失败CODE : {} ", response.getMessage(), response.getCode()));
                return false;
            }
            return true;
        } catch (ClientException e) {
            log.error("阿里云发送短信失败", e);
        }
        return false;
    }

    /**
     * 储存手机验证码到redis
     *
     * @param phone
     * @param code
     */
    private void storeCode(String phone, String code) {
        ALI_YUN_SMS_CACHE.put(gxCacheKeysUtils.getAliYunSMSCodeConfigKey(phone), code);
    }
}
