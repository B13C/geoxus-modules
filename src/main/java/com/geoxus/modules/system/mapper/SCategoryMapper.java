package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.system.builder.SCategoryBuilder;
import com.geoxus.modules.system.entity.SCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SCategoryMapper extends GXBaseMapper<SCategoryEntity> {
    @Override
    @SelectProvider(type = SCategoryBuilder.class, method = "listOrSearch")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = SCategoryBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict detail(Dict param);

    @SelectProvider(type = SCategoryBuilder.class, method = "getCategoryName")
    @Results({
            @Result(column = "category_name", property = "category_name", javaType = String.class)
    })
    String getCategoryName(Dict param);
}
