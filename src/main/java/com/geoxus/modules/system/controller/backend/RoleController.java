package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.system.entity.SRolesEntity;
import com.geoxus.modules.system.service.SRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/role/backend")
public class RoleController implements GXController<SRolesEntity> {
    @Autowired
    private SRolesService rolesService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation SRolesEntity target) {
        final long i = rolesService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation SRolesEntity target) {
        final long i = rolesService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = rolesService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(rolesService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(rolesService.detail(param));
    }
}
