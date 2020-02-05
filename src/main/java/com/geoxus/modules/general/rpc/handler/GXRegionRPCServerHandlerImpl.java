package com.geoxus.modules.general.rpc.handler;

import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.modules.general.service.SRegionService;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("regionRPCServerHandler")
public class GXRegionRPCServerHandlerImpl implements GXRPCServerHandler {
    @Autowired
    private SRegionService sRegionService;

    @Override
    public GXBaseService getService() {
        return sRegionService;
    }
}
