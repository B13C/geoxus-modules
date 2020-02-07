package com.geoxus.modules.goods.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToListTypeHandler;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.goods.builder.GoodsBuilder;
import com.geoxus.modules.goods.entity.GoodsEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper extends GXBaseMapper<GoodsEntity> {
    @SelectProvider(type = GoodsBuilder.class, method = "detail")
    @Results(id = "goodsResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "other", property = "other", typeHandler = GXJsonToListTypeHandler.class),
            @Result(column = "{model_id=goods_id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.mobile.core.framework.mapper.GXCoreMediaLibraryMapper.list"
            ))
    })
    Dict detail(Dict param);

    @Override
    @SelectProvider(type = GoodsBuilder.class, method = "listOrSearch")
    @ResultMap("goodsResult")
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = GoodsBuilder.class, method = "basicInfo")
    @Results(id = "goodsMediaResult", value = {
            @Result(column = "{model_id=goods_id,core_model_id=core_model_id}", property = "media", many = @Many(
                    select = "com.geoxus.core.framework.mapper.GXCoreMediaLibraryMapper.list"
            ))
    })
    Dict basicInfo(Dict param);
}
