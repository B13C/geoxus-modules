package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.service.GXCaptchaService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import com.geoxus.modules.system.service.SAdminService;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/backend")
public class AdminController implements GXController<SAdminEntity> {
    @Autowired
    private SAdminService sAdminService;

    @Autowired
    private GXCaptchaService captchaService;

    @Autowired
    private SAdminHasPermissionsService sAdminHasPermissionsService;

    @Autowired
    private SRoleHasPermissionsService sRoleHasPermissionsService;

    @Override
    @PostMapping("/create")
    @RequiresPermissions("admin-create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation SAdminEntity target) {
        final long i = sAdminService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(SAdminConstants.PRIMARY_KEY, i));
    }

    @Override
    @PostMapping("/update")
    @RequiresPermissions("admin-update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation(groups = {GXUpdateGroup.class}) SAdminEntity target) {
        final long i = sAdminService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(SAdminConstants.PRIMARY_KEY, i));
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("admin-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = sAdminService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @RequiresPermissions("admin-list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(sAdminService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    @RequiresPermissions("admin-detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = sAdminService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/change-password")
    @RequiresPermissions("admin-change-password")
    public GXResultUtils changePassword(@RequestBody Dict param) {
        param.set(SAdminConstants.PRIMARY_KEY, getUserIdFromToken(GXTokenManager.ADMIN_TOKEN, GXTokenManager.ADMIN_ID));
        final boolean b = sAdminService.changePassword(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/login")
    public GXResultUtils login(@RequestBody Dict param) {
        boolean flag = captchaService.checkCaptcha(param.getStr("uuid"), param.getStr("captcha"));
        if (!flag) {
            return GXResultUtils.error("验证码不正确");
        }
        final Dict dict = sAdminService.login(param);
        if (null != dict.getInt("code")) {
            return GXResultUtils.error(dict.getInt("code"), "账号或密码错误");
        }
        return GXResultUtils.ok().putData(dict);
    }

    @PostMapping("/add-admin-permissions")
    @RequiresPermissions("admin-add-admin-permissions")
    public GXResultUtils addAdminPermissions(@RequestBody Dict param) {
        final long adminId = param.getLong(GXTokenManager.ADMIN_ID);
        final List<Long> permissions = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj("permissions"));
        final boolean b = sAdminHasPermissionsService.addPermissionBatch(adminId, permissions);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/add-role-permissions")
    @RequiresPermissions("admin-add-role-permissions")
    public GXResultUtils addRolePermissions(@RequestBody Dict param) {
        final long roleId = param.getLong(SRolesConstants.PRIMARY_KEY);
        final List<Long> permissions = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj("permissions"));
        final boolean b = sRoleHasPermissionsService.addPermissionBatch(roleId, permissions);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/assign-role-to-admin")
    @RequiresPermissions("admin-assign-role-to-admin")
    public GXResultUtils assignRoleToAdmin(@RequestBody Dict param) {
        final List<Long> roleIds = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(SRolesConstants.PRIMARY_KEY));
        final Long adminId = param.getLong(SAdminConstants.PRIMARY_KEY);
        final boolean b = sAdminService.assignRolesToAdmin(adminId, roleIds);
        return GXResultUtils.ok().addKeyValue("status", b);
    }

    @PostMapping("/freeze")
    @RequiresPermissions("admin-freeze")
    public GXResultUtils freeze(@RequestBody Dict param) {
        final boolean b = sAdminService.freeze(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/unfreeze")
    @RequiresPermissions("admin-unfreeze")
    public GXResultUtils unfreeze(@RequestBody Dict param) {
        final boolean b = sAdminService.unfreeze(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
