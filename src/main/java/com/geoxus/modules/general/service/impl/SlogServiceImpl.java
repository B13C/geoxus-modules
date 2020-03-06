package com.geoxus.modules.general.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.general.entity.SlogEntity;
import com.geoxus.modules.general.mapper.SlogMapper;
import com.geoxus.modules.general.service.SlogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlogServiceImpl extends ServiceImpl<SlogMapper, SlogEntity> implements SlogService {
    @Override
    public List<SlogEntity> getLogsByStartTimeAndEndTime(int userId, long startAt, long endAt) {
        final List<SlogEntity> list;
        list = Optional
                .ofNullable(this.list(new QueryWrapper<SlogEntity>().eq("user_id", userId).ge("created_at", startAt).le("created_at", endAt)))
                .orElse(new ArrayList<>());
        return list;
    }

    @Override
    public long create(SlogEntity target, Dict param) {
        param.set("status", GXBusinessStatusCode.NORMAL.getCode());
        save(target);
        return target.getLogId();
    }

    @Override
    public long update(SlogEntity target, Dict param) {
        param.set("status", GXBusinessStatusCode.NORMAL.getCode());
        updateById(target);
        return target.getLogId();
    }

    @Override
    public boolean delete(Dict param) {
        return false;
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }
}
