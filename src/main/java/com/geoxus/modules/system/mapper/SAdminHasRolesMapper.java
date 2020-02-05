package com.geoxus.modules.system.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与管理员对应表
 */
@Mapper
public interface SAdminHasRolesMapper extends GXBaseMapper<SAdminHasRolesEntity> {

}
