package com.geoxus.modules.goods.event;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.modules.goods.entity.GoodsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GoodsEvent extends GXBaseEvent {
    private GoodsEntity targetEntity;

    public GoodsEvent(String eventName, GoodsEntity targetEntity, Dict param) {
        this.eventName = eventName;
        this.targetEntity = targetEntity;
        this.param = param;
    }
}
