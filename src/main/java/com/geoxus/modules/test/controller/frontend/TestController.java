package com.geoxus.modules.test.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXDurationCountLimitAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import com.geoxus.modules.test.entity.TestEntity;
import com.geoxus.modules.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-module/frontend")
public class TestController implements GXController<TestEntity> {
    @Autowired
    private GXRabbitMQRPCClientService rabbitMQRPCClientService;

    @Autowired
    private UUserService uUserService;

    @PostMapping("/test")
    @GXDurationCountLimitAnnotation(key = "hello")
    public GXResultUtils test(@RequestBody Dict param) {
        return GXResultUtils.ok();
    }
}
