package com.geoxus.modules.message.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.message.entity.MessageEntity;
import com.geoxus.modules.message.mapper.MessageMapper;
import com.geoxus.modules.message.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {
    @Override
    public long create(MessageEntity target, Dict param) {
        save(target);
        return target.getMessageId();
    }

    @Override
    public long update(MessageEntity target, Dict param) {
        updateById(target);
        return target.getMessageId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Optional.ofNullable(Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(getPrimaryKey()))).orElse(new ArrayList<>());
        return ids.isEmpty() || removeByIds(ids);
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
    }

    @Override
    public Dict detail(Dict param) {
        final Dict detail = baseMapper.detail(param);
        return detail;
    }
}
