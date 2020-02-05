package com.geoxus.modules.order.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.user.entity.UUserEntity;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;

public interface OrderService extends GXBusinessService<OrderEntity> {
    /**
     * 取消订单
     *
     * @param param
     * @return
     */
    boolean cancelOrderStatus(Dict param);

    /**
     * 支付宝支付信息
     *
     * @param param
     * @param userEntity
     * @return {"total_amount":"0.01" , "out_trade_no":"20190809100001","body":"body","subject":"subject"}
     */
    Dict aliPayInfo(Dict param, UUserEntity userEntity);

    /**
     * 微信支付信息
     *
     * @param param
     * @param userEntity
     * @return {"total_amount":"0.01","out_trade_no":"20190809100001","body":"body","scene_info":"场景值,参看官方文档"}
     */
    Dict weChatPayInfo(Dict param, UUserEntity userEntity);

    /**
     * 支付宝回调
     *
     * @param param
     * @return {"status":"支付宝要求返回的固定字符串"}
     */
    Dict aliPayCallBack(Dict param);

    /**
     * 微信回调
     *
     * @param param
     * @return {"status":"微信要求返回的固定字符串"}
     */
    Dict weChatPayCallBack(WxPayOrderNotifyResult param);
}
