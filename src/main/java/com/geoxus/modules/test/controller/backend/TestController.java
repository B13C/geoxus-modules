package com.geoxus.modules.test.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import com.geoxus.modules.test.entity.TestEntity;
import com.geoxus.modules.user.service.UUserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("backendTestController")
@RequestMapping("/test-module/backend")
public class TestController implements GXController<TestEntity> {
    @Autowired
    private GXRabbitMQRPCClientService rabbitMQRPCClientService;

    @Autowired
    private UUserService uUserService;

    @PostMapping("/test")
    //@RequiresPermissions(value = "backend-test-add")
    @RequiresRoles(value = {"admin"})
    public GXResultUtils test(@RequestBody Dict para) {
        return GXResultUtils.ok().putData(Dict.create().set("username", "britton").set("nick_name", "枫叶思源"));
    }
}
