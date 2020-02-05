package com.geoxus.modules.message.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.message.entity.UserHasMessageEntity;
import com.geoxus.modules.message.mapper.UserHasMessageMapper;
import com.geoxus.modules.message.service.MessageService;
import com.geoxus.modules.message.service.UserHasMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserHasMessageServiceImpl extends ServiceImpl<UserHasMessageMapper, UserHasMessageEntity> implements UserHasMessageService {
    @Autowired
    private MessageService messageService;

    @Override
    @Transactional
    public long create(UserHasMessageEntity target, Dict param) {
        final UserHasMessageEntity entity = getOne(new QueryWrapper<UserHasMessageEntity>().allEq(Dict.create().set(GXTokenManager.USER_ID, target.getUserId()).set("message_id", target.getMessageId())));
        if (null != entity) {
            return entity.getId();
        }
        final Dict detail = messageService.detail(Dict.create().set(MessageService.PRIMARY_KEY, target.getMessageId()));
        if (null == detail) {
            throw new GXException("消息不存在");
        }
        target = modifyEntityJSONFieldMultiValue(target, Dict.create().set("ext", Dict.create().set("status", GXBusinessStatusCode.NORMAL.getCode()).set("title", detail.getStr("title")).set("content", detail.getStr("content"))));
        saveOrUpdate(target);
        return target.getId();
    }

    @Override
    @Transactional
    public long update(UserHasMessageEntity target, Dict param) {
        final UserHasMessageEntity one = getOne(new QueryWrapper<UserHasMessageEntity>().allEq(Dict.create().set(GXTokenManager.USER_ID, target.getUserId()).set("message_id", target.getMessageId())));
        if (null != one) {
            return 0;
        }
        saveOrUpdate(target);
        return target.getId();
    }

    @Override
    public boolean delete(Dict param) {
        return batchDelete(param);
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return null;
    }

    @Override
    public Dict detail(Dict param) {
        return null;
    }

    @Override
    public GXPagination unReadMessage(Dict param) {
        final Integer userId = param.getInt(GXTokenManager.USER_ID);
        if (null == userId) {
            return new GXPagination(Collections.emptyList());
        }
        final IPage<Dict> page = constructPageObjectFromParam(param);
        final IPage<Dict> iPage = baseMapper.unReadMessage(page, param, Dict.class);
        return new GXPagination(iPage.getRecords(), iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }

    @Override
    public GXPagination readMessage(Dict param) {
        final IPage<Dict> page = constructPageObjectFromParam(param);
        final IPage<Dict> iPage = baseMapper.readMessage(page, param, Dict.class);
        return new GXPagination(iPage.getRecords(), iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }
}
