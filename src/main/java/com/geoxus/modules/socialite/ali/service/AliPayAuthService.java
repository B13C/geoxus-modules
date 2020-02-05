package com.geoxus.modules.socialite.ali.service;

import com.alipay.api.response.AlipayUserUserinfoShareResponse;

import java.util.Map;

/**
 * 支付宝用户认证相关Service
 */
public interface AliPayAuthService {
    /**
     * 根据authCode获得AccessToken
     *
     * @param authCode     authCode
     * @param aliPayUserId aliPayUserId
     * @return AccessToken
     */
    String userAuth(String authCode, String aliPayUserId);

    /**
     * 使用accessToken获取用户信息
     *
     * @param accessToken token
     * @return AlipayUserUserinfoShareResponse
     */
    AlipayUserUserinfoShareResponse getAlipayUserInfo(String accessToken);

    /**
     * 支付宝授权加签
     *
     * @return 签名后字符串
     */
    String rsaSign();

    /**
     * 签名检测
     *
     * @param params
     * @return
     */
    boolean rsaCheckV1(Map<String, String> params);
}
