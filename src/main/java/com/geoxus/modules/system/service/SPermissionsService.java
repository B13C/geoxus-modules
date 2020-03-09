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
     * 获取管理员的所有权限列表
     * 权限包括:
     * <p>
     * 1、分配给角色的权限
     * 2、直接分配给管理员的权限
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return
     */
    Set<String> getAdminAllPermissions(Long adminId);

    /**
     * 根据权限ID获取权限码
     *
     * @param permissionIds 权限码列表
     * @return Set<String>
     */
    Set<String> getPermissionsCode(List<Integer> permissionIds);
}

