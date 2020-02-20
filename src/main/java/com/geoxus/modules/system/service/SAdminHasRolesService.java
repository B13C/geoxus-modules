package com.geoxus.modules.system.service;

import com.geoxus.core.common.service.GXSAdminHasRolesService;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;

import java.util.List;

public interface SAdminHasRolesService extends GXSAdminHasRolesService<SAdminHasRolesEntity> {
    /**
     * 给管理员新增角色
     *
     * @param adminId 管理员ID
     * @param roleIds 角色ID
     * @return
     */
    boolean addRoleToAdmin(long adminId, List<Long> roleIds);
}

