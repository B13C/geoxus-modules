package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SMenuBuilder;
import com.geoxus.modules.system.entity.SMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Set;

@Mapper
public interface SMenuMapper extends GXBaseMapper<SMenuEntity> {
    @SelectProvider(type = SMenuBuilder.class, method = "detail")
    Dict detail(Dict param);

    @SelectProvider(type = SMenuBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearch(Dict param);

    @SelectProvider(type = SMenuBuilder.class, method = "getAllPerms")
    Set<String> getAllPerms(Long adminId);

    @SelectProvider(type = SMenuBuilder.class, method = "getAllMenuId")
    List<Integer> getAllMenuId(Long adminId);
}
