package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
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
     * 获取权限树
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
     * 获取角色的权限id
     *
     * @return
     */
    @PostMapping("/get-role-permissions")
    @RequiresPermissions("sys-permissions-get-role-permissions")
    public GXResultUtils getRolePermissions(@RequestBody Dict param) {
        List<Integer> list = sPermissionsService.getRolePermissions(param.getInt("role_id"));
        return GXResultUtils.ok().putData(list);
    }

    @PostMapping("/get-admin-permissions")
    public GXResultUtils getAdminPermissions(@RequestBody Dict param) {
        final List<Dict> list = sAdminHasPermissionsService.listOrSearch(param);
        return GXResultUtils.ok().putData(list);
    }

    /**
     * 修改权限
     *
     * @param param
     * @return
     */
    @PostMapping("/update-role-permissions")
    @RequiresPermissions("sys-permissions-update-role-permissions")
    public GXResultUtils updateRolePermissions(@RequestBody Dict param) {
        sPermissionsService.updateRolePermissions(param);
        return GXResultUtils.ok();
    }

    /**
     * 获取当前登录人的所有权限码集合
     *
     * @return GXResultUtils
     */
    @PostMapping("/get-admin-all-permissions")
    public GXResultUtils getAdminAllPermissions() {
        final long adminId = GXHttpContextUtils.getUserIdFromToken(GXTokenManager.ADMIN_TOKEN, GXTokenManager.ADMIN_ID);
        Set<String> set = sPermissionsService.getAdminAllPermissions(adminId);
        return GXResultUtils.ok().putData(set);
    }
}
