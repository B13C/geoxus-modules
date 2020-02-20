package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SRoleHasPermissionsBuilder;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 角色与权限的对应表
 */
@Mapper
public interface SRoleHasPermissionsMapper extends GXBaseMapper<SRoleHasPermissionsEntity> {
    @SelectProvider(type = SRoleHasPermissionsBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearch(Dict param);
}
