package com.geoxus.modules.system.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与权限的对应表
 */
@Mapper
public interface SRoleHasPermissionsMapper extends GXBaseMapper<SRoleHasPermissionsEntity> {

}
