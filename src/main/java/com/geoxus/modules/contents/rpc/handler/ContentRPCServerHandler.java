package com.geoxus.modules.contents.rpc.handler;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.contents.service.ContentService;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("contentRPCServerHandler")
public class ContentRPCServerHandler implements GXRPCServerHandler {
    @Autowired
    private ContentService contentService;

    @Override
    public GXBusinessService getService() {
        return contentService;
    }
}
