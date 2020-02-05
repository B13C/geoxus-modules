package com.geoxus.modules.goods.listener;

import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.listener.GXSyncBaseListener;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.modules.goods.event.GoodsEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsListener extends GXSyncBaseListener {
    public GoodsListener() {
        GXSyncEventBusCenterUtils.getInstance().register(this);
    }

    @Subscribe
    public void listener(GoodsEvent event) {
        final String str = "guava eventBus source : {} , entity : {} , param : {}";
        final String s = StrUtil.format(str, event.getEventName(), event.getTargetEntity(), event.getParam());
        log.info(s);
    }
}
