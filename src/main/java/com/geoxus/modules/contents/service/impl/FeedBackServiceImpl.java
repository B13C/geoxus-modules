package com.geoxus.modules.contents.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        saveOrUpdate(target);
        return target.getFeedbackId();
    }

    @Override
    public long update(FeedBackEntity target, Dict param) {
        saveOrUpdate(target);
        return target.getFeedbackId();
    }

    @Override
    public boolean delete(Dict param) {
        final int feedbackId = param.getInt(FeedBackConstants.PRIMARY_KEY);
        return updateJSONFieldSingleValue(getById(feedbackId), "ext.status", GXBusinessStatusCode.DELETED.getCode());
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean replay(Dict param) {
        FeedBackEntity feedBackEntity = getById(param.getLong(getPrimaryKey()));
        final boolean b = updateById(modifyEntityJSONFieldMultiValue(feedBackEntity, param));
        return b;
    }
}
