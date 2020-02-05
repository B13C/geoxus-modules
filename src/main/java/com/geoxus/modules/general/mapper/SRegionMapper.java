package com.geoxus.modules.general.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.general.builder.RegionBuilder;
import com.geoxus.modules.general.entity.SRegionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface SRegionMapper extends GXBaseMapper<SRegionEntity> {
    @SelectProvider(type = RegionBuilder.class, method = "areaInfo")
    Dict areaInfo(Dict param);
}
