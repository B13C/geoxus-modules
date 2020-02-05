package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.CommonOperationLogEntity;
import com.geoxus.modules.system.mapper.CommonOperationLogMapper;
import com.geoxus.modules.system.service.CommonOperationLogService;
import org.springframework.stereotype.Service;

@Service
public class CommonOperationLogServiceImpl extends ServiceImpl<CommonOperationLogMapper, CommonOperationLogEntity> implements CommonOperationLogService {
    @Override
    public long create(CommonOperationLogEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(CommonOperationLogEntity target, Dict param) {
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
