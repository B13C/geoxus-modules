package com.geoxus.modules.contents.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.contents.builder.FeedBackBuilder;
import com.geoxus.modules.contents.entity.FeedBackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface FeedBackMapper extends GXBaseMapper<FeedBackEntity> {
    @Override
    @SelectProvider(type = FeedBackBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
    })
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = FeedBackBuilder.class, method = "detail")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
    })
    Dict detail(Dict pram);

    @SelectProvider(type = FeedBackBuilder.class, method = "countByCondition")
    int countByCondition(Dict param);
}
