package com.geoxus.modules.general.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.geoxus.core.common.annotation.GXApiIdempotentAnnotation;
import com.geoxus.core.common.annotation.GXDurationCountLimitAnnotation;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXCacheKeysUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.modules.general.config.NetEaseSMSConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 网易云短信服务
 */
@Service
@Slf4j
@ConditionalOnExpression("'${sms-provider}'.equals('netease-sms')")
public class GXNetEaseSMSServiceImpl implements GXSendSMSService {
    @GXFieldCommentAnnotation(zh = "Guava缓存组件")
    private static final Cache<String, String> NET_EASE_SMS_CACHE;

    static {
        NET_EASE_SMS_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(Duration.ofSeconds(300L)).build();
    }

    @Autowired
    private GXCacheKeysUtils gxCacheKeysUtils;

    @Autowired
    private NetEaseSMSConfig netEaseSMSConfig;

    @Override
    @GXApiIdempotentAnnotation(expires = 10)
    @GXDurationCountLimitAnnotation(key = "net:ease:sms")
    public GXResultUtils send(String phone, String templateName, Dict param) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> postParams = new HashMap<>();
        // 1.设置公共参数
        postParams.put("secretId", netEaseSMSConfig.getSecretId());
        postParams.put("businessId", netEaseSMSConfig.getBussinessId());
        postParams.put("version", netEaseSMSConfig.getVersion());
        postParams.put("timestamp", String.valueOf(System.currentTimeMillis()));
        postParams.put("nonce", IdUtil.simpleUUID());
        postParams.put("mobile", phone);
        postParams.put("templateId", netEaseSMSConfig.getTemplateId());
        String codeName = "code";
        String verificationCode = Optional.ofNullable(param.getStr(codeName)).orElse(RandomUtil.randomNumbers(netEaseSMSConfig.getCodeLen()));
        postParams.put("params", netEaseSMSConfig.getParams() + verificationCode);
        //需要上行的时候,这里需要设置为true
        postParams.put("needUp", "true");
        // 3.生成签名信息
        String signature;
        try {
            signature = genSignature(netEaseSMSConfig.getSecretKey(), postParams);
        } catch (Exception e) {
            log.error("网易云短信接口签名错误", e);
            return GXResultUtils.error(GXResultCode.SMS_SEND_FAILURE);
        }
        postParams.put("signature", signature);
        try {
            String s = HttpUtil.post(netEaseSMSConfig.getApiUrl(), postParams);
            storeCode(phone, verificationCode);
            result.put("msg", s);
            data.put("data", result);
        } catch (Exception e) {
            log.error("网易云短信接口发送错误", e);
            return GXResultUtils.error(GXResultCode.SMS_SEND_FAILURE);
        }
        return GXResultUtils.error(GXResultCode.SMS_SEND_SUCCESS);
    }

    /**
     * 储存手机验证码到redis
     *
     * @param phone 手机号码
     * @param code  验证码
     */
    private void storeCode(String phone, String code) {
        NET_EASE_SMS_CACHE.put(gxCacheKeysUtils.getNetEaseSMSCodeConfigKey(phone), code);
    }

    /**
     * 验证手机验证码
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return boolean
     */
    public boolean verification(String phone, String code) {
        if (StrUtil.isEmpty(phone) || StrUtil.isEmpty(code)) {
            return false;
        }
        String key = gxCacheKeysUtils.getNetEaseSMSCodeConfigKey(phone);
        String s = NET_EASE_SMS_CACHE.getIfPresent(key);
        if (code.equalsIgnoreCase(s)) {
            NET_EASE_SMS_CACHE.invalidate(key);
            return true;
        }
        return false;
    }

    /**
     * 生成签名信息
     *
     * @param secretKey 产品私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     */
    private String genSignature(String secretKey, Map<String, Object> params) throws UnsupportedEncodingException {
        // 1. 参数名按照ASCII码表升序排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 2. 按照排序拼接参数名与参数值
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        // 3. 将secretKey拼接到最后
        sb.append(secretKey);
        // 4. MD5是128位长度的摘要算法，转换为十六进制之后长度为32字符
        return DigestUtils.md5Hex(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
