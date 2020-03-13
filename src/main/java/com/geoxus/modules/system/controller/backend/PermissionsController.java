package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.service.SPermissionsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/permissions/backend")
public class PermissionsController implements GXController<SPermissionsEntity> {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Override
    @PostMapping("/create")
    @RequiresPermissions("permissions-create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation SPermissionsEntity target) {
        final long l = sPermissionsService.create(target, Dict.create());
        return GXResultUtils.ok().addKeyValue(SPermissionsConstants.PRIMARY_KEY, l);
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("permissions-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean status = sPermissionsService.delete(param);
        return GXResultUtils.ok().addKeyValue("status", status);
    }

    /**
     * 获取所有权限的树状结构
     *
     * @return
     */
    @PostMapping("/get-permissions-tree")
    @RequiresPermissions("permissions-get-permissions-tree")
    public GXResultUtils getPermissionsTree() {
        List<SPermissionsEntity> list = sPermissionsService.getPermissionsTree();
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
    @RequiresPermissions("permissions-get-admin-all-permissions")
    public GXResultUtils getAdminAllPermissions(@RequestBody Dict param) {
        Long adminId = param.getLong(SAdminConstants.PRIMARY_KEY);
        if (null == adminId) {
            adminId = GXHttpContextUtils.getUserIdFromToken(GXTokenManager.ADMIN_TOKEN, GXTokenManager.ADMIN_ID);
        }
        Set<String> set = sPermissionsService.getAdminAllPermissions(adminId);
        return GXResultUtils.ok().putData(set);
    }
}
