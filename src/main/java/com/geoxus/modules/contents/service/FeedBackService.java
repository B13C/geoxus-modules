package com.geoxus.modules.contents.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.contents.constant.FeedBackConstants;
import com.geoxus.modules.contents.entity.FeedBackEntity;

public interface FeedBackService extends GXBusinessService<FeedBackEntity> {
    /**
     * 回复
     *
     * @param param
     * @return
     */
    boolean replay(Dict param);

    default String getPrimaryKey() {
        return FeedBackConstants.PRIMARY_KEY;
    }
}
