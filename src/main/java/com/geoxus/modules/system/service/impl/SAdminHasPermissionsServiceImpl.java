package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SAdminHasPermissionsEntity;
import com.geoxus.modules.system.mapper.SAdminHasPermissionsMapper;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import org.springframework.stereotype.Service;

@Service
public class SAdminHasPermissionsServiceImpl extends ServiceImpl<SAdminHasPermissionsMapper, SAdminHasPermissionsEntity> implements SAdminHasPermissionsService {
    @Override
    public long create(SAdminHasPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(SAdminHasPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public boolean delete(Dict param) {
        return false;
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return null;
    }

    @Override
    public Dict detail(Dict param) {
        return null;
    }
}
