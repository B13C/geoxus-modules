package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;
import com.geoxus.modules.system.mapper.SAdminHasRolesMapper;
import com.geoxus.modules.system.service.SAdminHasRolesService;
import org.springframework.stereotype.Service;

@Service
public class SAdminHasRolesServiceImpl extends ServiceImpl<SAdminHasRolesMapper, SAdminHasRolesEntity> implements SAdminHasRolesService {
    @Override
    public long create(SAdminHasRolesEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(SAdminHasRolesEntity target, Dict param) {
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
