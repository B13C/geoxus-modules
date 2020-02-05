package com.geoxus.modules.test.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-module/frontend")
public class TestController implements GXController<Dict> {
    @Autowired
    private GXRabbitMQRPCClientService rabbitMQRPCClientService;

    @Autowired
    private UUserService uUserService;

    @PostMapping("/test")
    @GXLoginAnnotation
    public GXResultUtils test(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity uUserEntity) {

        final String queueName = GXCommonUtils.getRemoteRPCServerValueByKey("abiz-server", "server-name");
        final Dict call = rabbitMQRPCClientService.call("testMethod", "btTestServerHandler", param, queueName);
        return GXResultUtils.ok().putData(call);
    }
}
