package com.geoxus.modules.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.modules.user.entity.SUserTokenEntity;
import com.geoxus.modules.user.mapper.SUserTokenMapper;
import com.geoxus.modules.user.service.SUserTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SUserTokenServiceImpl extends ServiceImpl<SUserTokenMapper, SUserTokenEntity> implements SUserTokenService {
    private static final int USER_TYPE = 2;

    @Override
    public boolean createOrUpdate(String token, long userId) {
        SUserTokenEntity userTokenEntity = Optional.ofNullable(this.getById(userId)).orElse(new SUserTokenEntity());
        userTokenEntity.setToken(token);
        userTokenEntity.setUserId(userId);
        userTokenEntity.setType(USER_TYPE);
        userTokenEntity.setExpiredAt((int) (DateUtil.currentSeconds() + GXTokenManager.EXPIRES));
        return saveOrUpdate(userTokenEntity);
    }

    @Override
    public boolean logout(Dict param) {
        final Long userId = param.getLong(GXTokenManager.USER_ID);
        if (null == userId) {
            return false;
        }
        SUserTokenEntity userToken = this.getById(userId);
        if (null == userToken) {
            return false;
        }
        userToken.setExpiredAt(0);
        return updateById(userToken);
    }
}
