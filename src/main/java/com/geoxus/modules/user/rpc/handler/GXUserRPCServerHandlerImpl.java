package com.geoxus.modules.user.rpc.handler;

import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import com.geoxus.modules.user.service.UUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("userRPCServerHandler")
public class GXUserRPCServerHandlerImpl implements GXRPCServerHandler {
    @Autowired
    private UUserService uUserService;

    @Override
    public GXBaseService getService() {
        return uUserService;
    }
}
