package com.geoxus.modules.user.listener;

import com.geoxus.core.common.listener.GXSyncBaseListener;
import com.geoxus.modules.user.event.UWithdrawEvent;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UWithdrawListener extends GXSyncBaseListener {
    @Subscribe
    @AllowConcurrentEvents
    public void listener(UWithdrawEvent event) {
        final long userId = event.getTargetEntity().getUserId();
        final double amount = Optional.ofNullable(event.getParam().getDouble("amount")).orElse(0.00);
    }
}
