package com.geoxus.modules.system.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SRolesBuilder;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Set;

/**
 * 角色与管理员对应表
 */
@Mapper
public interface SAdminHasRolesMapper extends GXBaseMapper<SAdminHasRolesEntity> {
    @SelectProvider(type = SRolesBuilder.class, method = "getRoleNameByAdminId")
    Set<String> getRoleNameByAdminId(long adminId);
}
