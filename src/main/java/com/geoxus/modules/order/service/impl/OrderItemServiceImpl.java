package com.geoxus.modules.order.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.goods.entity.GoodsEntity;
import com.geoxus.modules.goods.service.GoodsService;
import com.geoxus.modules.order.entity.OrderItemEntity;
import com.geoxus.modules.order.mapper.OrderItemMapper;
import com.geoxus.modules.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemEntity> implements OrderItemService {
    @Autowired
    private GoodsService goodsService;

    @Override
    @GXLoginAnnotation
    public long create(OrderItemEntity target, Dict param) {
        final boolean b = saveOrUpdate(target);
        return target.getOrderItemId();
    }

    @Override
    @GXLoginAnnotation
    public long update(OrderItemEntity target, Dict param) {
        final boolean b = saveOrUpdate(target);
        return target.getOrderItemId();
    }

    @Override
    @GXLoginAnnotation
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(PRIMARY_KEY, param.getInt(PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    @GXLoginAnnotation
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    @Override
    @GXLoginAnnotation
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean splitItem(long orderNumber, int userId, List<JSONObject> items) {
        for (JSONObject goodsInfo : items) {
            final Integer goodsId = goodsInfo.getInt("goods_id");
            final Integer quantity = goodsInfo.getInt("quantity");
            final GoodsEntity goods = goodsService.getById(goodsId);
            for (int i = 0; i < quantity; i++) {
                final OrderItemEntity orderItem = new OrderItemEntity();
                final Dict extData = Dict.create().set("consumer_code", RandomUtil.randomNumbers(8));
                extData.set("status", GXBusinessStatusCode.NORMAL.getCode()).putAll(compositeGoodsInfo(goods));
                orderItem.setCoreModelId(0);
                orderItem.setUserId(userId);
                orderItem.setGoodsId(goodsId);
                orderItem.setQuantity(1);
                orderItem.setGoodsPrice(0.00);
                orderItem.setPurchasingPrice(0.00);
                orderItem.setExt(JSONUtil.toJsonStr(extData));
                create(orderItem, null);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean mergeItem(long orderNumber, int userId, List<JSONObject> items) {
        for (JSONObject goodsInfo : items) {
            final Integer goodsId = goodsInfo.getInt("goods_id");
            final Integer quantity = goodsInfo.getInt("quantity");
            final GoodsEntity goods = goodsService.getById(goodsId);
            final OrderItemEntity orderItem = new OrderItemEntity();
            final Dict extData = Dict.create().set("other_info", RandomUtil.randomNumbers(8));
            extData.set("status", GXBusinessStatusCode.NORMAL.getCode()).putAll(compositeGoodsInfo(goods));
            orderItem.setCoreModelId(0);
            orderItem.setUserId(userId);
            orderItem.setGoodsId(goodsId);
            orderItem.setQuantity(quantity);
            orderItem.setGoodsPrice(0.00);
            orderItem.setPurchasingPrice(0.00);
            final String s = JSONUtil.toJsonStr(extData);
            orderItem.setExt(s);
            create(orderItem, null);
        }
        return true;
    }

    /**
     * 组合订单数据
     *
     * @param goods
     * @return
     */
    private Dict compositeGoodsInfo(GoodsEntity goods) {
        final Dict dict = Dict.create();
        final String goodsName = goodsService.getSingleJSONFieldValueByDB(GoodsEntity.class, "ext->>'$.name'", String.class, dict);
        final Float price = goodsService.getSingleJSONFieldValueByDB(GoodsEntity.class, "ext->>'$.price'", Float.class, dict);
        return dict.set("name", goodsName).set("price", price);
    }
}
