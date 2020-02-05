package com.geoxus.modules.user.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geoxus.modules.user.entity.SUserTokenEntity;

public interface SUserTokenService extends IService<SUserTokenEntity> {
    /**
     * 创建或者更新用户Token
     *
     * @param token
     * @param userId
     * @return
     */
    boolean createOrUpdate(String token, long userId);

    /**
     * 登出
     *
     * @param param
     * @return
     */
    boolean logout(Dict param);
}
