package com.geoxus.modules.contents.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.contents.builder.ContentBuilder;
import com.geoxus.modules.contents.entity.ContentEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ContentMapper extends GXBaseMapper<ContentEntity> {
    @SelectProvider(type = ContentBuilder.class, method = "detail")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{category_id = category_id}", property = "category_name", one = @One(
                    select = "com.geoxus.modules.system.mapper.SCategoryMapper.getCategoryName",
                    fetchType = FetchType.EAGER
            ), javaType = String.class),
            @Result(column = "{object_id=content_id , core_model_id=core_model_id}", property = "comments", many = @Many(
                    select = "com.geoxus.modules.contents.mapper.CommentMapper.listOrSearch",
                    fetchType = FetchType.EAGER
            ))
    })
    Dict detail(Dict param);

    @Override
    @SelectProvider(type = ContentBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{category_id = category_id}", property = "category_name", one = @One(
                    select = "com.geoxus.modules.system.mapper.SCategoryMapper.getCategoryName",
                    fetchType = FetchType.EAGER
            ), javaType = String.class),
            @Result(column = "{object_id=content_id , core_model_id=core_model_id}", property = "comments", many = @Many(
                    select = "com.geoxus.modules.contents.mapper.CommentMapper.listOrSearch",
                    fetchType = FetchType.EAGER
            ))
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);
}
