package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.entity.SMenuEntity;
import com.geoxus.modules.system.service.SMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController("backendMenuController")
@RequestMapping("/menu/backend")
public class MenuController implements GXController<SMenuEntity> {
    @Autowired
    private SMenuService sMenuService;

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final List<Dict> menuTree = sMenuService.getTree();
        return GXResultUtils.ok().putData(menuTree);
    }

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@GXRequestBodyToEntityAnnotation @Valid SMenuEntity target) {
        final long l = sMenuService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(SMenuConstants.PRIMARY_KEY, l));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@GXRequestBodyToEntityAnnotation @Valid SMenuEntity target) {
        final long l = sMenuService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(SMenuConstants.PRIMARY_KEY, l));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(Dict param) {
        return null;
    }
}
