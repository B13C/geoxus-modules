package com.geoxus.modules.user.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.user.builder.UWithdrawBuilder;
import com.geoxus.modules.user.entity.UWithdrawEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface UWithdrawMapper extends GXBaseMapper<UWithdrawEntity> {
    @Override
    @SelectProvider(type = UWithdrawBuilder.class, method = "listOrSearch")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = UWithdrawBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict detail(Dict param);
}
