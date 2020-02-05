package com.geoxus.modules.test.rpc.services;

import cn.hutool.core.lang.Dict;

public interface BTTestService {
    Dict testMethod(Dict param) throws InterruptedException;
}
