package com.geoxus.modules.socialite.ali.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.geoxus.modules.pay.ali.config.AliPayConfig;
import com.geoxus.modules.pay.ali.exception.AliPayException;
import com.geoxus.modules.socialite.ali.enums.AliPayAuthErrorCode;
import com.geoxus.modules.socialite.ali.service.AliPayAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AliPayAuthServiceImpl implements AliPayAuthService {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Override
    public String userAuth(String authCode, String aliPayUserId) {
        //创建API对应的request类
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);
        //通过alipayClient调用API，获得对应的response类
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            throw new AliPayException(504, AliPayAuthErrorCode.ACCESS_TOKEN_ERROR.getId(), AliPayAuthErrorCode.ACCESS_TOKEN_ERROR.getMsg());
        }
        //根据response中的结果继续业务逻辑处理
        if (response == null) {
            return "";
        }
        if (!response.getUserId().equals(aliPayUserId)) {
            return "";
        }
        return response.getAccessToken();
    }

    @Override
    public AlipayUserUserinfoShareResponse getAlipayUserInfo(String accessToken) {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.user.userinfo.share
        AlipayUserUserinfoShareRequest request = new AlipayUserUserinfoShareRequest();
        //授权类接口执行API调用时需要带上accessToken
        AlipayUserUserinfoShareResponse response = null;
        try {
            response = alipayClient.execute(request, accessToken);
        } catch (AlipayApiException e) {
            log.error("支付宝-使用accessToken获取用户信息失败", e);
        }
        return response;
    }

    @Override
    public String rsaSign() {
        Map<String, String> params = new HashMap<>();
        // 服务接口名称， 固定值
        params.put("apiname", "com.alipay.account.auth");
        params.put("method", "alipay.open.auth.sdk.code.get");
        params.put("app_id", aliPayConfig.getAppId());
        // 商户类型标识， 固定值
        params.put("app_name", "mc");
        // 商户签约拿到的pid，如：2088102123816631
        params.put("pid", aliPayConfig.getPid());
        // 业务类型， 固定值
        params.put("biz_type", "openservice");
        // 产品码， 固定值
        params.put("product_id", "APP_FAST_LOGIN");
        // 授权范围， 固定值
        params.put("scope", "kuaijie");
        // 商户唯一标识，如：kkkkk091125
        params.put("target_id", new Date().toString());
        // 授权类型， 固定值
        params.put("auth_type", "AUTHACCOUNT");
        // 签名类型
        params.put("sign_type", AlipayConstants.SIGN_TYPE_RSA2);
        try {
            String sign = AlipaySignature.rsaSign(AlipaySignature.getSignContent(params), aliPayConfig.getMerchantPrivateKey(), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            return AlipaySignature.getSignContent(params) + "&sign=" + URLEncoder.encode(sign, "UTF-8");
        } catch (Exception e) {
            log.error("授权签名失败", e);
            throw new AliPayException("授权签名失败");
        }
    }

    @Override
    public boolean rsaCheckV1(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, aliPayConfig.getResAliPayPublicKey(), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
        } catch (AlipayApiException e) {
            log.error("异步返回结果验签失败", e);
        }
        return false;
    }
}
