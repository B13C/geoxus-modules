package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import com.geoxus.modules.system.mapper.SRoleHasPermissionsMapper;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SRoleHasPermissionsServiceImpl extends ServiceImpl<SRoleHasPermissionsMapper, SRoleHasPermissionsEntity> implements SRoleHasPermissionsService {
    @Override
    public long create(SRoleHasPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(SRoleHasPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public boolean delete(Dict param) {
        return false;
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return null;
    }

    @Override
    public List<Dict> listOrSearch(Dict param) {
        return baseMapper.listOrSearch(param);
    }

    @Override
    public Dict detail(Dict param) {
        return null;
    }
}
