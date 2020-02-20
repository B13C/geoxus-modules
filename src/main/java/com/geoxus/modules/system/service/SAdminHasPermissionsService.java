package com.geoxus.modules.system.service;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.system.entity.SAdminHasPermissionsEntity;

import java.util.List;

public interface SAdminHasPermissionsService extends GXBusinessService<SAdminHasPermissionsEntity> {
    /**
     * 给管理员新增权限
     *
     * @param adminId     管理员ID
     * @param permissions 权限ID组
     * @return
     */
    boolean addPermissionBatch(long adminId, List<Long> permissions);
}

