package com.geoxus.modules.general.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.general.builder.SlogBuilder;
import com.geoxus.modules.general.entity.SlogEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SlogMapper extends GXBaseMapper<SlogEntity> {
    @Override
    @SelectProvider(type = SlogBuilder.class, method = "listOrSearch")
    @Results(id = "slogResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = SlogBuilder.class, method = "detail")
    @ResultMap("slogResult")
    Dict detail(Dict param);
}
