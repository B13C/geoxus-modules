package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SAdminHasPermissionsBuilder;
import com.geoxus.modules.system.entity.SAdminHasPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SAdminHasPermissionsMapper extends GXBaseMapper<SAdminHasPermissionsEntity> {
    @SelectProvider(type = SAdminHasPermissionsBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearch(Dict param);
}
