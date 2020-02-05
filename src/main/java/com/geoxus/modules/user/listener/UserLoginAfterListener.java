package com.geoxus.modules.user.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.listener.GXAsyncBaseListener;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.modules.user.constant.UUserConstants;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.entity.UserLogEntity;
import com.geoxus.modules.user.event.UserLoginAfterEvent;
import com.geoxus.modules.user.service.UUserService;
import com.geoxus.modules.user.service.UserLogService;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLoginAfterListener extends GXAsyncBaseListener {
    @Autowired
    private UUserService uUserService;

    @Autowired
    private UserLogService userLogService;

    @Subscribe
    @AllowConcurrentEvents
    public void listener(UserLoginAfterEvent event) {
        final UUserEntity uUserEntity = event.getTargetEntity();
        final String eventName = event.getEventName();
        final Dict param = event.getParam();
        final long userId = uUserEntity.getUserId();
        final UserLogEntity entity = new UserLogEntity();
        entity.setCoreModelId(UUserConstants.CORE_MODEL_ID);
        entity.setEventName(eventName);
        entity.setExt(JSONUtil.toJsonStr(param));
        entity.setUserId(userId);
        entity.setAction("用户登录");
        entity.setIp(GXHttpContextUtils.getIP());
        entity.setUpdatedAt((int) DateUtil.currentSeconds());
        userLogService.save(entity);
    }
}
