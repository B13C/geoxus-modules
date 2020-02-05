package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXSPermissionsService;
import com.geoxus.modules.system.entity.SPermissionsEntity;

import java.util.List;

public interface SPermissionsService extends GXSPermissionsService<SPermissionsEntity> {
    List<SPermissionsEntity> getPermissionsTree();

    /**
     * 修改角色权限
     *
     * @param request
     */
    void updateRolePermissions(Dict request);
}

