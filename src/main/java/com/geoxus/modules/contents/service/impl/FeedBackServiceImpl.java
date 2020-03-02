package com.geoxus.modules.contents.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.contents.constant.FeedBackConstants;
import com.geoxus.modules.contents.entity.FeedBackEntity;
import com.geoxus.modules.contents.mapper.FeedBackMapper;
import com.geoxus.modules.contents.service.FeedBackService;
import org.springframework.stereotype.Service;

@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBackEntity> implements FeedBackService {
    @Override
    public long create(FeedBackEntity target, Dict param) {
        save(target);
        return target.getFeedbackId();
    }

    @Override
    public long update(FeedBackEntity target, Dict param) {
        updateById(target);
        return target.getFeedbackId();
    }

    @Override
    public boolean delete(Dict param) {
        final int feedbackId = param.getInt(FeedBackConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(FeedBackConstants.PRIMARY_KEY, feedbackId);
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean replay(Dict param) {
        final Long remove = Convert.convert(Long.class, param.remove(getPrimaryKey()));
        FeedBackEntity feedBackEntity = getById(remove);
        if (feedBackEntity.getStatus() == FeedBackConstants.REPLY) {
            return false;
        }
        feedBackEntity.setStatus(FeedBackConstants.REPLY);
        return updateById(feedBackEntity);
    }
}
