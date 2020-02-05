package com.geoxus.modules.contents.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToListTypeHandler;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.contents.builder.ContentBuilder;
import com.geoxus.modules.contents.entity.ContentEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper
@Primary
public interface ContentMapper extends GXBaseMapper<ContentEntity> {
    @SelectProvider(type = ContentBuilder.class, method = "detail")
    @Results(id = "contentResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "items", property = "items", typeHandler = GXJsonToListTypeHandler.class),
            @Result(column = "{model_id=id , core_model_id=core_model_id}", property = "comments", many = @Many(
                    select = "com.geoxus.modules.contents.mapper.CommentMapper.listOrSearch"
            ))
    })
    Dict detail(Dict param);

    @Override
    @SelectProvider(type = ContentBuilder.class, method = "listOrSearch")
    @ResultMap("contentResult")
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);
}
