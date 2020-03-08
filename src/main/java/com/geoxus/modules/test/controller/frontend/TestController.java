package com.geoxus.modules.test.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import com.geoxus.modules.test.entity.TestEntity;
import com.geoxus.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test-module/frontend")
public class TestController implements GXController<TestEntity> {
    @Autowired
    private GXRabbitMQRPCClientService rabbitMQRPCClientService;

    @Autowired
    private UUserService uUserService;

    @Autowired
    private List<CacheManager> cacheManagers;

    @PostMapping("/test")
    //@GXDurationCountLimitAnnotation(key = "hello")
    public GXResultUtils test(@RequestBody Dict param) {
        for (CacheManager c : cacheManagers) {
            System.out.println(c);
        }
        return GXResultUtils.ok();
    }
}
