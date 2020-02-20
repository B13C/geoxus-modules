package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.service.GXCaptchaService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.service.*;
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
    private SRolesService sRolesService;

    @Autowired
    private SAdminHasPermissionsService sAdminHasPermissionsService;

    @Autowired
    private SRoleHasPermissionsService sRoleHasPermissionsService;

    @Autowired
    private SAdminHasRolesService sAdminHasRolesService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation SAdminEntity target) {
        final long i = sAdminService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation SAdminEntity target) {
        final long i = sAdminService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = sAdminService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(sAdminService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = sAdminService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/change-password")
    public GXResultUtils changePassword(@RequestBody Dict param) {
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
    public GXResultUtils addAdminPermissions(@RequestBody Dict param) {
        final long adminId = param.getLong(GXTokenManager.ADMIN_ID);
        final List<Long> permissions = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj("permissions"));
        final boolean b = sAdminHasPermissionsService.addPermissionBatch(adminId, permissions);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/add-role-permissions")
    public GXResultUtils addRolePermissions(@RequestBody Dict param) {
        final long roleId = param.getLong(SRolesConstants.PRIMARY_KEY);
        final List<Long> permissions = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj("permissions"));
        final boolean b = sRoleHasPermissionsService.addPermissionBatch(roleId, permissions);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/assign-role-to-admin")
    public GXResultUtils assignRoleToAdmin(@RequestBody Dict param) {
        final List<Long> roleIds = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(SRolesConstants.PRIMARY_KEY));
        final Long adminId = param.getLong(SAdminConstants.PRIMARY_KEY);
        final boolean b = sAdminService.addRoleToAdmin(adminId, roleIds);
        return GXResultUtils.ok().addKeyValue("status", b);
    }

    @PostMapping("/freeze")
    public GXResultUtils freeze(@RequestBody Dict param) {
        final boolean b = sAdminService.freeze(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/unfreeze")
    public GXResultUtils unfreeze(@RequestBody Dict param) {
        final boolean b = sAdminService.unfreeze(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
