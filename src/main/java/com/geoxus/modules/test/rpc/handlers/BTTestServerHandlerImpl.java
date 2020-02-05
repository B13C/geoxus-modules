package com.geoxus.modules.test.rpc.handlers;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import com.geoxus.modules.test.rpc.services.BTTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component("btTestServerHandler")
public class BTTestServerHandlerImpl implements GXRPCServerHandler {
    @Autowired
    private BTTestService btTestService;

    @Override
    public JSONObject rpcHandler(Dict param) {
        final Dict rpcInfo = getRPCCallInfo(param);
        final Method method = ReflectUtil.getMethodByName(btTestService.getClass(), rpcInfo.getStr("method"));
        if (null != method) {
            final Object invoke = ReflectUtil.invokeWithCheck(btTestService, method, param);
            return JSONUtil.parseObj(invoke);
        }
        return JSONUtil.parseObj(Dict.create().set("msg", StrUtil.format("{}不存在", method.getName())));
    }
}
