package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
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
     * 修改角色权限
     *
     * @param requestParam 请求参数
     */
    void updateRolePermissions(Dict requestParam);

    /**
     * 获取管理员的权限列表
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return
     */
    Set<String> getAdminAllPermissions(long adminId);
}

