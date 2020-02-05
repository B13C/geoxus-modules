package com.geoxus.modules.user.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.user.entity.UWithdrawEntity;

public interface UWithdrawService extends GXBusinessService<UWithdrawEntity> {
    /**
     * 拒绝提现
     *
     * @param param
     * @return
     */
    boolean decline(Dict param);
}
