package com.geoxus.modules.order.event;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.modules.order.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderAfterEvent extends GXBaseEvent {
    private OrderEntity targetEntity;

    public OrderAfterEvent(String eventName, OrderEntity targetEntity, Dict param) {
        this.eventName = eventName;
        this.targetEntity = targetEntity;
        this.param = param;
    }
}
