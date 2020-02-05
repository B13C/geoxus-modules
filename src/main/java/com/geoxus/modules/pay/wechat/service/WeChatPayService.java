package com.geoxus.modules.pay.wechat.service;

import cn.hutool.core.lang.Dict;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;

public interface WeChatPayService {
    /**
     * 获取支付服务
     *
     * @param tradeType
     * @return
     */
    WxPayService getWxPayService(String tradeType);

    /**
     * 组合支付订单请求数据
     *
     * @param param
     * @return
     */
    WxPayUnifiedOrderRequest combineOrderRequestData(Dict param);

    /**
     * 初始化支付信息
     *
     * @param tradeType
     * @return
     */
    WxPayConfig wxPayConfig(String tradeType);
}
