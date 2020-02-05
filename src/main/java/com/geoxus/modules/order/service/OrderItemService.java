package com.geoxus.modules.order.service;

import cn.hutool.json.JSONObject;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.order.entity.OrderItemEntity;

import java.util.List;

public interface OrderItemService extends GXBusinessService<OrderItemEntity> {
    /**
     * 主键名字
     */
    String PRIMARY_KEY = "item_id";

    /**
     * 一个数量存储一条记录
     */
    int ITEM_HANDLE_SPLIT = 1;

    /**
     * 每个商品存储一条记录
     */
    int ITEM_HANDLE_MERGE = 2;

    /**
     * 一个商品数量存储一条记录
     */
    boolean splitItem(long orderNumber, int userId, List<JSONObject> items);

    /**
     * 每个商品存储一条记录
     */
    boolean mergeItem(long orderNumber, int userId, List<JSONObject> items);

    /**
     * 获取 Primary Key
     *
     * @return
     */
    default String getPrimaryKey() {
        return PRIMARY_KEY;
    }
}
