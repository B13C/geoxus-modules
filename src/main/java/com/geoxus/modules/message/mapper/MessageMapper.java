package com.geoxus.modules.message.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.message.builder.MessageBuilder;
import com.geoxus.modules.message.entity.MessageEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {
    @SelectProvider(type = MessageBuilder.class, method = "listOrSearch")
    @Results(id = "messageResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
    })
    List<Dict> listOrSearch(Dict param);
    
    @SelectProvider(type = MessageBuilder.class, method = "detail")
    @ResultMap("messageResult")
    Dict detail(Dict param);
}
