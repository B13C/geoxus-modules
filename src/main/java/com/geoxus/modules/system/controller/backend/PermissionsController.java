package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import com.geoxus.modules.system.service.SPermissionsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/permissions/backend")
public class PermissionsController {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Autowired
    private SAdminHasPermissionsService sAdminHasPermissionsService;

    /**
     * 获取所有权限的树状结构
     *
     * @return
     */
    @PostMapping("/get-permissions-tree")
    @RequiresPermissions("sys-permissions-get-permissions-tree")
    public GXResultUtils getPermissionsTree() {
        List<SPermissionsEntity> list = sPermissionsService.getPermissionsTree();
        return GXResultUtils.ok().putData(list);
    }

    /**
     * 获取分配给角色的权限
     *
     * @return
     */
    @PostMapping("/get-role-permissions")
    @RequiresPermissions("sys-permissions-get-role-permissions")
    public GXResultUtils getRolePermissions(@RequestBody Dict param) {
        List<Long> list = sPermissionsService.getRolePermissions(param.getLong(SRolesConstants.PRIMARY_KEY));
        return GXResultUtils.ok().putData(list);
    }

    /**
     * 获取直接分配给管理员的权限
     *
     * @param param
     * @return
     */
    @PostMapping("/get-admin-permissions")
    public GXResultUtils getAdminPermissions(@RequestBody Dict param) {
        final List<Dict> list = sAdminHasPermissionsService.listOrSearch(param);
        return GXResultUtils.ok().putData(list);
    }

    /**
     * 获取指定登录人的所有权限码集合
     * 权限包括:
     * <p>
     * 1、角色权限
     * 2、直接分配的权限
     *
     * @return GXResultUtils
     */
    @PostMapping("/get-admin-all-permissions")
    public GXResultUtils getAdminAllPermissions(@RequestBody Dict param) {
        Long adminId = param.getLong(SAdminConstants.PRIMARY_KEY);
        if (null == adminId) {
            adminId = GXHttpContextUtils.getUserIdFromToken(GXTokenManager.ADMIN_TOKEN, GXTokenManager.ADMIN_ID);
        }
        Set<String> set = sPermissionsService.getAdminAllPermissions(adminId);
        return GXResultUtils.ok().putData(set);
    }
}
