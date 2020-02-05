package com.geoxus.modules.contents.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.contents.entity.ContentEntity;

public interface ContentService extends GXBusinessService<ContentEntity> {
    /**
     * 显示
     *
     * @param param
     * @return
     */
    boolean show(Dict param);

    /**
     * 隐藏
     *
     * @param param
     * @return
     */
    boolean hidden(Dict param);

    /**
     * 用于测试RPC调用
     *
     * @param param
     * @return
     */
    Dict testRPC(Dict param);
}
