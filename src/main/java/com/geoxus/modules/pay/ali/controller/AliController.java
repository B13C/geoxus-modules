package com.geoxus.modules.pay.ali.controller;

import cn.hutool.core.lang.Dict;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.order.constant.OrderConstants;
import com.geoxus.modules.pay.ali.config.AliPayConfig;
import com.geoxus.modules.pay.ali.constant.AliPayTradeConstants;
import com.geoxus.modules.pay.ali.service.AliPayTradeService;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.order.service.OrderService;
import com.geoxus.modules.user.entity.UUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pay/mobile")
public class AliController {
    @Autowired
    private AliPayTradeService aliPayTradeService;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private OrderService orderService;

    /**
     * 支付宝——APP支付
     *
     * @param param
     * @param user
     * @return
     */
    @PostMapping("/ali-app-pay")
    @GXLoginAnnotation
    public GXResultUtils aliAppPay(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final Long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
        if (null == orderSn) {
            return GXResultUtils.error("订单号必传！");
        }
        OrderEntity orderEntity = orderService.getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, param.getLong(OrderConstants.PRIMARY_KEY)));
        if (null == orderEntity) {
            return GXResultUtils.error("该订单不存在！");
        }
        final Dict payInfo = orderService.aliPayInfo(param, user);
        payInfo.set("product_code", AliPayTradeConstants.ALI_PAY_PRODUCT_CODE);
        final String result = aliPayTradeService.appPay(payInfo);
        return GXResultUtils.ok().putData(Dict.create().set("resp_body", result));
    }

    /**
     * 支付宝——网页支付
     *
     * @param param
     * @param user
     * @return
     */
    @PostMapping("/ali-page-pay")
    @GXLoginAnnotation
    public GXResultUtils aliPagePay(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final Long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
        if (null == orderSn) {
            return GXResultUtils.error("订单号必传！");
        }
        OrderEntity orderEntity = orderService.getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, param.getLong(OrderConstants.PRIMARY_KEY)));
        if (null == orderEntity) {
            return GXResultUtils.error("该订单不存在！");
        }
        final Dict payInfo = orderService.aliPayInfo(param, user);
        payInfo.set("product_code", AliPayTradeConstants.ALI_PAY_PRODUCT_CODE);
        payInfo.set("notify_url", aliPayConfig.getNotifyUrl());
        final String result = aliPayTradeService.pagePay(payInfo);
        return GXResultUtils.ok().putData(Dict.create().set("resp_body", result));
    }

    @PostMapping("/ali-notify")
    public String weChatNotify(HttpServletRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean flag = AlipaySignature.rsaCheckV1(params, aliPayConfig.getResAliPayPublicKey(), "UTF-8", "RSA2");
        if (!flag) {
            return "fail";
        }
        //商户订单号
        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //支付宝交易号
        String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //交易总金额
        String totalAmount = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Dict respDict = Dict.create().set("order_number", outTradeNo).set("total_amount", totalAmount).set("trade_no", tradeNo);
        orderService.aliPayCallBack(respDict);
        return "success";
    }
}
