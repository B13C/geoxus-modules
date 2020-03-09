package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SPermissionsBuilder;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Set;

/**
 * 权限列表
 */
@Mapper
public interface SPermissionsMapper extends GXBaseMapper<SPermissionsEntity> {
    @SelectProvider(type = SPermissionsBuilder.class, method = "getAdminAllPermissions")
    Set<String> getAdminAllPermissions(Dict param);

    @SelectProvider(type = SPermissionsBuilder.class, method = "getAllPermissionCode")
    Set<String> getAllPermissionCode();

    @SelectProvider(type = SPermissionsBuilder.class, method = "getPermissionsCode")
    Set<String> getPermissionsCode(String permissionIds);
}
