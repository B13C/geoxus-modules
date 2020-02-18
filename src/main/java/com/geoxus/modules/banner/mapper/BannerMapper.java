package com.geoxus.modules.banner.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.banner.builder.BannerBuilder;
import com.geoxus.modules.banner.entity.BannerEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BannerMapper extends GXBaseMapper<BannerEntity> {
    @Override
    @SelectProvider(type = BannerBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{model_id=banner_id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.GXCoreMediaLibraryMapper.getMediaByCondition"
            ), javaType = List.class)
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = BannerBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{model_id=banner_id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.GXCoreMediaLibraryMapper.getMediaByCondition"
            ), javaType = List.class)
    })
    Dict detail(Dict param);
}
