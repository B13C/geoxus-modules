package com.geoxus.modules.order.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.listener.GXSyncBaseListener;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.order.event.OrderAfterEvent;
import com.geoxus.modules.order.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderAfterEventListener extends GXSyncBaseListener {
    @Autowired
    private OrderItemService orderItemService;

    @Subscribe
    @AllowConcurrentEvents
    public void addOrderItem(OrderAfterEvent event) {
        log.info("OrderAfterEventListener");
        if (null != event.getParam().getObj("items")) {
            final OrderEntity entity = event.getTargetEntity();
            final Integer flag = event.getParam().getInt("flag");
            final long orderId = entity.getOrderSn();
            final Integer userId = event.getParam().getInt("user_id");
            final List<JSONObject> items = Convert.convert(new TypeReference<List<JSONObject>>() {
            }, JSONUtil.parseArray(event.getParam().getObj("items")));
            boolean result = false;
            if (flag == OrderItemService.ITEM_HANDLE_SPLIT) {
                result = orderItemService.splitItem(Convert.convert(Long.class, orderId), userId, items);
            } else if (flag == OrderItemService.ITEM_HANDLE_MERGE) {
                result = orderItemService.mergeItem(Convert.convert(Long.class, orderId), userId, items);
            }
            log.info("OrderListener : {} ", result);
        }
    }
}
