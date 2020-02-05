package com.geoxus.modules.user.event;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.modules.user.entity.UUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSaveBeforeEvent extends GXBaseEvent {
    private UUserEntity targetEntity;

    public UserSaveBeforeEvent(String eventName, UUserEntity targetEntity, Dict param) {
        this.eventName = eventName;
        this.targetEntity = targetEntity;
        this.param = param;
    }
}
