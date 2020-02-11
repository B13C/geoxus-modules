package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SRolesEntity;
import com.geoxus.modules.system.mapper.SRolesMapper;
import com.geoxus.modules.system.service.SRolesService;
import org.springframework.stereotype.Service;

@Service
public class SRolesServiceImpl extends ServiceImpl<SRolesMapper, SRolesEntity> implements SRolesService {
    @Override
    public long create(SRolesEntity target, Dict param) {
        final boolean b = save(target);
        return target.getRoleId();
    }

    @Override
    public long update(SRolesEntity target, Dict param) {
        final boolean b = updateById(target);
        return target.getRoleId();
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(PRIMARY_KEY, param.getInt(PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), GXBaseBuilderConstants.NON_OPERATOR, condition);
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        final Dict detail = baseMapper.detail(param);
        return detail;
    }
}
