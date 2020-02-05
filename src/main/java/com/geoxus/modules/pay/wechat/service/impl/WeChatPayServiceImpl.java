package com.geoxus.modules.pay.wechat.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.geoxus.core.common.config.GXWeChatConfig;
import com.geoxus.modules.pay.wechat.service.WeChatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class WeChatPayServiceImpl implements WeChatPayService {
    @Autowired
    private GXWeChatConfig weChatConfig;

    @Override
    public WxPayService getWxPayService(String tradeType) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig(tradeType));
        return wxPayService;
    }

    @Override
    public WxPayUnifiedOrderRequest combineOrderRequestData(Dict param) {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        String nonceStr = RandomUtil.randomString(32).toUpperCase();
        final int totalFee = (int) (param.getFloat("total_amount") * 100);
        request.setNonceStr(nonceStr);
        request.setBody(param.getStr("body"));
        request.setOutTradeNo(param.getStr("out_trade_no"));
        request.setTotalFee(totalFee);
        request.setSceneInfo(param.getStr("scene_info"));
        request.setSpbillCreateIp(ServletUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
        request.setNotifyUrl(weChatConfig.getNotifyUrl());
        return request;
    }

    @Override
    public WxPayConfig wxPayConfig(String tradeType) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(weChatConfig.getAppId());
        wxPayConfig.setMchId(weChatConfig.getMchId());
        wxPayConfig.setTradeType(tradeType);
        wxPayConfig.setMchKey(weChatConfig.getMchKey());
        wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
        wxPayConfig.setNotifyUrl(weChatConfig.getNotifyUrl());
        wxPayConfig.setKeyPath(weChatConfig.getKeyPath());
        return wxPayConfig;
    }
}
