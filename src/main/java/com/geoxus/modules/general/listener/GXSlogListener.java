package com.geoxus.modules.general.listener;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.event.GXSlogEvent;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.general.entity.SlogEntity;
import com.geoxus.modules.general.service.SlogService;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class GXSlogListener {
    @Autowired
    private SlogService sLogService;

    @Subscribe
    @AllowConcurrentEvents
    public void record(GXSlogEvent event) {
        final Dict param = event.getParam();
        final String source = event.getSource();
        final Object target = event.getTarget();
        final SlogEntity entity = new SlogEntity();
        Objects.requireNonNull(param.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_NAME));
        Objects.requireNonNull(param.getLong("model_id"));
        Objects.requireNonNull(source);
        if (null == param.getStr("remark")) {
            param.set("remark", "默认备注信息");
        }
        if (null == param.getInt("status")) {
            param.set("status", GXBusinessStatusCode.NORMAL.getCode());
        }
        if (null == param.getStr("logs")) {

        }
        entity.setCoreModelId(param.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_NAME));
        entity.setModelId(param.getLong("model_id"));
        entity.setExt(JSONUtil.parse(target).toString());
        entity.setSource(source);
        entity.setUserId(Optional.ofNullable(param.getLong("user_id")).orElse(0L));
        sLogService.save(entity);
    }
}
