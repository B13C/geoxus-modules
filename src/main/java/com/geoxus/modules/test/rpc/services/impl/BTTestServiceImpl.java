package com.geoxus.modules.test.rpc.services.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.modules.test.rpc.services.BTTestService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BTTestServiceImpl implements BTTestService {
    @Override
    public Dict testMethod(Dict param) throws InterruptedException {
        if (null != param.getInt("sleep")) {
            Thread.sleep(param.getInt("sleep"));
        }
        Objects.requireNonNull(param.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_NAME));
        final Dict set = Dict.create().set("thread_name", Thread.currentThread().getName()).set("aassddd", "sdadsad").set("name", "jack").set("other", "ABIZ项目的其他信息").set("date", DateUtil.currentSeconds());
        return set;
    }
}
