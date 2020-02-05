package com.geoxus.modules.pay.wechat.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.order.constant.OrderConstants;
import com.geoxus.modules.pay.wechat.service.WeChatPayService;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.order.service.OrderService;
import com.geoxus.modules.user.entity.UUserEntity;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/pay/mobile")
@Slf4j
public class WeChatController {
    @Autowired
    private WeChatPayService wechatPayService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/wx-js-pay")
    @GXLoginAnnotation
    public GXResultUtils weChatJsPay(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        WxPayMpOrderResult result;
        try {
            final Long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
            if (null == orderSn) {
                return GXResultUtils.error("订单号必传！");
            }
            OrderEntity orderEntity = orderService.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", param.getLong("order_sn")));
            if (null == orderEntity) {
                return GXResultUtils.error("该订单不存在！");
            }
            final Dict payInfo = orderService.weChatPayInfo(param, user);
            result = wechatPayService.getWxPayService(WxPayConstants.TradeType.JSAPI).createOrder(wechatPayService.combineOrderRequestData(payInfo));
        } catch (WxPayException e) {
            log.error("微信生成订单失败:{}", e.getStackTrace(), e);
            return GXResultUtils.error(e.getCustomErrorMsg());
        }
        return GXResultUtils.ok().putData(Dict.create().set("resp_body", formatForWeChatPayUniApp(result)));
    }

    @PostMapping("/wx-app-pay")
    @GXLoginAnnotation
    public GXResultUtils weChatAppPay(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        WxPayAppOrderResult result;
        try {
            final Long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
            if (null == orderSn) {
                return GXResultUtils.error("订单号必传！");
            }
            OrderEntity orderEntity = orderService.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", param.getLong("order_sn")));
            if (null == orderEntity) {
                return GXResultUtils.error("该订单不存在！");
            }
            final Dict payInfo = orderService.weChatPayInfo(param, user);
            result = wechatPayService.getWxPayService(WxPayConstants.TradeType.APP).createOrder(wechatPayService.combineOrderRequestData(payInfo));
        } catch (WxPayException e) {
            log.error("微信生成订单失败:{}", e.getStackTrace(), e);
            return GXResultUtils.error(e.getCustomErrorMsg());
        }
        return GXResultUtils.ok().putData(Dict.create().set("resp_body", JSONUtil.toJsonStr(formatForWeChatPayUniApp(result))));
    }

    @PostMapping("/wx-h5-pay")
    @GXLoginAnnotation
    public GXResultUtils weChatH5Pay(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        WxPayAppOrderResult result;
        try {
            final Long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
            if (null == orderSn) {
                return GXResultUtils.error("订单号必传！");
            }
            OrderEntity entity = orderService.getById(orderSn);
            if (null == entity) {
                return GXResultUtils.error("该订单不存在！");
            }
            final Dict payInfo = orderService.weChatPayInfo(param, user);
            result = wechatPayService.getWxPayService(WxPayConstants.TradeType.MWEB).createOrder(wechatPayService.combineOrderRequestData(payInfo));
        } catch (WxPayException e) {
            log.error("微信生成订单失败:{}", e.getStackTrace(), e);
            return GXResultUtils.error(e.getCustomErrorMsg());
        }
        return GXResultUtils.ok().putData(Dict.create().set("resp_body", JSONUtil.toJsonStr(formatForWeChatPayUniApp(result))));
    }

    @PostMapping("/wx-notify")
    public String weChatNotify(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wechatPayService.wxPayConfig(""));
            final WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
            final Dict dict = orderService.weChatPayCallBack(result);
            log.info("微信支付回调 : " + result.toString());
            return dict.getStr("status");
        } catch (WxPayException e) {
            log.error("获取微信回调失败:{}", e.getStackTrace(), e);
        } catch (Exception e) {
            log.error("获取微信回调失败,通用异常:{}", e.getStackTrace(), e);
        }
        return "error";
    }

    /**
     * 格式微信支付参数 UniApp 不能直接使用微信接口返回的数据
     *
     * @param payInfo
     * @return
     */
    private Dict formatForWeChatPayUniApp(Object payInfo) {
        Dict dict = JSONUtil.toBean(JSONUtil.toJsonStr(payInfo), Dict.class);
        Set<String> keySet = dict.keySet();
        Dict formatDict = Dict.create();
        for (String key : keySet) {
            if (key.equals("packageValue")) {
                formatDict.set("package", dict.getStr(key));
                continue;
            }
            formatDict.set(key.toLowerCase(), dict.getStr(key));
        }
        return formatDict;
    }
}
