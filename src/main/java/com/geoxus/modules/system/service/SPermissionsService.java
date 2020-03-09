package com.geoxus.modules.system.service;

import com.geoxus.core.common.service.GXSPermissionsService;
import com.geoxus.modules.system.entity.SPermissionsEntity;

import java.util.List;
import java.util.Set;

public interface SPermissionsService extends GXSPermissionsService<SPermissionsEntity> {
    /**
     * 获取权限的树形列表
     *
     * @return 返回权限列表
     */
    List<SPermissionsEntity> getPermissionsTree();

    /**
     * 根据权限ID获取权限码
     *
     * @param permissionIds 权限码列表
     * @return Set<String>
     */
    Set<String> getPermissionsCode(List<Integer> permissionIds);
}

