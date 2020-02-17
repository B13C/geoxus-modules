package com.geoxus.modules.banner.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.banner.constant.BannerConstants;
import com.geoxus.modules.banner.entity.BannerEntity;

public interface BannerService extends GXBusinessService<BannerEntity> {
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

    @Override
    default String getPrimaryKey() {
        return BannerConstants.PRIMARY_KEY;
    }
}
