package com.geoxus.modules.contents.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.contents.builder.CommentBuilder;
import com.geoxus.modules.contents.entity.CommentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper extends GXBaseMapper<CommentEntity> {
    @Override
    @SelectProvider(type = CommentBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{model_id=id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.CoreMediaLibraryMapper.list"
            ))
    })
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = CommentBuilder.class, method = "detail")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{model_id=id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.CoreMediaLibraryMapper.list"
            ))
    })
    Dict detail(Dict param);
}
