package com.geoxus.modules.order.event;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.modules.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemEvent extends GXBaseEvent {
    private OrderItemEntity targetEntity;

    public OrderItemEvent(String eventName, OrderItemEntity targetEntity, Dict param) {
        this.eventName = eventName;
        this.targetEntity = targetEntity;
        this.param = param;
    }
}
