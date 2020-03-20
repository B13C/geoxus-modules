package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.GXSyslogEntity;
import com.geoxus.modules.system.mapper.GXSyslogMapper;
import com.geoxus.modules.system.service.GXSyslogService;
import org.springframework.stereotype.Service;

@Service
public class GXSyslogServiceImpl extends ServiceImpl<GXSyslogMapper, GXSyslogEntity> implements GXSyslogService {
    @Override
    public long create(GXSyslogEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(GXSyslogEntity target, Dict param) {
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
    public Dict detail(Dict param) {
        return null;
    }
}
