package com.geoxus.modules.message.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.message.builder.UserHasMessageBuilder;
import com.geoxus.modules.message.entity.UserHasMessageEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserHasMessageMapper extends GXBaseMapper<UserHasMessageEntity> {
    @SelectProvider(type = UserHasMessageBuilder.class, method = "readMessage")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    <R> IPage<R> readMessage(IPage<R> page, Dict param, Class<R> clazz);

    @UpdateProvider(type = UserHasMessageBuilder.class, method = "batchDeleteByIds")
    boolean batchDeleteByIds(List<Integer> list);

    @SelectProvider(type = UserHasMessageBuilder.class, method = "unReadMessage")
    <R> IPage<R> unReadMessage(IPage<R> page, Dict param, Class<R> clazz);
}
