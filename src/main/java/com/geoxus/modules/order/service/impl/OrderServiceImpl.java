package com.geoxus.modules.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.config.GXWeChatConfig;
import com.geoxus.core.common.event.GXSlogEvent;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.goods.entity.GoodsEntity;
import com.geoxus.modules.goods.service.GoodsService;
import com.geoxus.modules.order.constant.OrderConstants;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.order.event.OrderAfterEvent;
import com.geoxus.modules.order.event.OrderBeforeEvent;
import com.geoxus.modules.order.mapper.OrderMapper;
import com.geoxus.modules.order.service.OrderItemService;
import com.geoxus.modules.order.service.OrderService;
import com.geoxus.modules.pay.ali.config.AliPayConfig;
import com.geoxus.modules.user.constant.UUserConstants;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.service.UUserService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    @Autowired
    private UUserService uUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private GXWeChatConfig weChatConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(OrderEntity target, Dict param) {
        final long userId = param.getLong(GXTokenManager.USER_ID);
        param.set("current_timestamp", DateUtil.currentSeconds());
        if (null != param.getObj("items")) {
            final double totalAmount = calculateTotalAmount(Convert.convert(new TypeReference<List<JSONObject>>() {
            }, param.getObj("items")));
            target.setOrderPrice(totalAmount);
        }
        final OrderBeforeEvent orderBeforeEvent = new OrderBeforeEvent("新增订单之前", target, param);
        GXSyncEventBusCenterUtils.getInstance().post(orderBeforeEvent);
        target.setUserId(userId);
        target.setUsername(uUserService.getEntitySingleField(Dict.create().set(UUserConstants.PRIMARY_KEY, userId), "username", String.class, "defaultUserName"));
        final boolean b = save(target);
        if (b) {
            final OrderAfterEvent orderAfterEvent = new OrderAfterEvent("新增订单之后", target, param);
            GXSyncEventBusCenterUtils.getInstance().post(orderAfterEvent);
            return Convert.toLong(target.getOrderSn());
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long update(OrderEntity target, Dict param) {
        final long userId = param.getLong(GXTokenManager.USER_ID);
        return 0;
    }

    @Override
    public boolean delete(Dict param) {
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), param);
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean cancelOrderStatus(Dict param) {
        final Long orderNumber = param.getLong(OrderConstants.PRIMARY_KEY);
        OrderEntity orderEntity = getById(orderNumber);
        return updateById(orderEntity);
    }

    @Override
    public Dict aliPayInfo(Dict param, UUserEntity userEntity) {
        final OrderEntity entity = getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, param.getLong(OrderConstants.PRIMARY_KEY)));
        param.set("out_trade_no", entity.getOrderSn());
        param.set("total_amount", entity.getOrderPrice());
        param.set("notify_url", aliPayConfig.getNotifyUrl());
        param.set("subject", aliPayConfig.getSubject());
        param.set("product_code", Optional.ofNullable(param.getStr("product_code")).orElse("QUICK_MSECURITY_PAY"));
        param.set("body", aliPayConfig.getBody());
        return param;
    }

    @Override
    public Dict weChatPayInfo(Dict param, UUserEntity userEntity) {
        final OrderEntity entity = getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, param.getLong(OrderConstants.PRIMARY_KEY)));
        param.set("out_trade_no", entity.getOrderSn());
        param.set("total_amount", entity.getOrderPrice());
        param.set("body", weChatConfig.getBody());
        param.set("notify_url", weChatConfig.getNotifyUrl());
        return param;
    }

    @Override
    public Dict aliPayCallBack(Dict param) {
        long orderSn = param.getLong(OrderConstants.PRIMARY_KEY);
        OrderEntity target = getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, orderSn));
        final Dict data = Dict.create();
        data.set("order_sn", orderSn);
        data.set("price", param.getDouble("total_amount"));
        data.set("pay_type", "aliPay");
        data.set("pay_platform", "支付宝支付");
        data.set("status", GXBusinessStatusCode.NORMAL.getCode());
        final boolean b = updateById(target);
        final GXSlogEvent<OrderEntity> slogEvent = new GXSlogEvent<>("aliPay", target, "s_log", Dict.create());
        GXSyncEventBusCenterUtils.getInstance().post(slogEvent);
        return Dict.create().set("status", b);
    }

    @Override
    public Dict weChatPayCallBack(WxPayOrderNotifyResult param) {
        long orderSn = Long.parseLong(param.getOutTradeNo());
        OrderEntity target = getOne(new QueryWrapper<OrderEntity>().eq(OrderConstants.PRIMARY_KEY, orderSn));
        final Dict data = Dict.create();
        data.set("order_sn", orderSn);
        data.set("price", param.getTotalFee() / 100);
        data.set("pay_type", "wechat");
        data.set("pay_platform", "微信支付");
        data.set("status", GXBusinessStatusCode.NORMAL.getCode());
        final boolean b = updateById(target);
        final GXSlogEvent<OrderEntity> slogEvent = new GXSlogEvent<>("wechatPay", target, "s_log", Dict.create());
        GXSyncEventBusCenterUtils.getInstance().post(slogEvent);
        return Dict.create().set("status", b);
    }

    private double calculateTotalAmount(List<JSONObject> param) {
        double totalAmount = 0.00;
        for (JSONObject info : param) {
            final int goodsId = info.getInt("goods_id");
            final GoodsEntity goodsEntity = goodsService.getById(goodsId);
            if (null != goodsEntity) {
                final double price = goodsService.getSingleJSONFieldValueByDB(GoodsEntity.class, "ext->>'$.price'", Double.class, Dict.create());
                final int quantity = info.getInt("quantity");
                totalAmount += price * quantity;
            }
        }
        return totalAmount;
    }
}
