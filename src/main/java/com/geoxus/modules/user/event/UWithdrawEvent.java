package com.geoxus.modules.user.event;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.modules.user.entity.UWithdrawEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UWithdrawEvent extends GXBaseEvent {
    private UWithdrawEntity targetEntity;

    public UWithdrawEvent(String eventName, UWithdrawEntity targetEntity, Dict param) {
        this.eventName = eventName;
        this.targetEntity = targetEntity;
        this.param = param;
    }
}
