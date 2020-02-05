package com.geoxus.modules.message.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.message.entity.UserHasMessageEntity;

public interface UserHasMessageService extends GXBusinessService<UserHasMessageEntity> {
    /**
     * 获取未读消息
     *
     * @param param
     * @return
     */
    GXPagination unReadMessage(Dict param);

    /**
     * 获取已读消息
     *
     * @param param
     * @return
     */
    GXPagination readMessage(Dict param);
}