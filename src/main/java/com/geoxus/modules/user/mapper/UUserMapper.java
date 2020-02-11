package com.geoxus.modules.user.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.user.builder.UserBuilder;
import com.geoxus.modules.user.entity.UUserEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.IntegerTypeHandler;

import java.util.List;

@Mapper
public interface UUserMapper extends GXBaseMapper<UUserEntity> {
    @Override
    @SelectProvider(type = UserBuilder.class, method = "listOrSearch")
    @Results(id = "userResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{user_id=user_id,parent_id=parent_id}", property = "fansCount", one = @One(
                    select = "com.geoxus.modules.user.mapper.UUserMapper.fansCount"
            ), javaType = Integer.class),
            @Result(column = "{model_id=user_id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.GXCoreMediaLibraryMapper.getMediaByCondition"
            ))
    })
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = UserBuilder.class, method = "children")
    @ResultMap("userResult")
    List<Dict> children(Dict param);

    @SelectProvider(type = UserBuilder.class, method = "detail")
    @ResultMap("userResult")
    Dict detail(Dict param);

    @SelectProvider(type = UserBuilder.class, method = "fansCount")
    @Results(value = {
            @Result(column = "count", property = "count", typeHandler = IntegerTypeHandler.class)
    })
    int fansCount(Dict param);

    @SelectProvider(type = UserBuilder.class, method = "specialInfo")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{model_id=id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.GXCoreMediaLibraryMapper.list"
            ))
    })
    Dict specialInfo(Dict param);

    @SelectProvider(type = UserBuilder.class, method = "baseInfoDetail")
    Dict baseInfoDetail(Dict param, List<String> fields);
}
