package com.geoxus.modules.system.service;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;

import java.util.List;

public interface SRoleHasPermissionsService extends GXBusinessService<SRoleHasPermissionsEntity> {
    /**
     * 给角色新增权限
     *
     * @param roleId      管理员ID
     * @param permissions 权限ID组
     * @return
     */
    boolean addPermissionBatch(long roleId, List<Long> permissions);
}

