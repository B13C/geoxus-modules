package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SRolesBuilder;
import com.geoxus.modules.system.entity.SAdminRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 角色与管理员对应表
 */
@Mapper
public interface SAdminRolesMapper extends GXBaseMapper<SAdminRolesEntity> {
    @SelectProvider(type = SRolesBuilder.class, method = "getRolesByAdminId")
    List<Dict> getRolesByAdminId(long adminId);
}
