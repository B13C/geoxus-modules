package com.geoxus.modules.system.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SCategoryEntity;
import com.geoxus.modules.system.service.SCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController("backendCategoryController")
@RequestMapping("/category/backend")
public class CategoryController implements GXController<SCategoryEntity> {
    @Autowired
    private SCategoryService categoryService;

    @Override
    @PostMapping("/create")
    @RequiresPermissions("category-create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation SCategoryEntity target) {
        final long i = categoryService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("category_id", i));
    }

    @Override
    @PostMapping("/update")
    @RequiresPermissions("category-update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation SCategoryEntity target) {
        final long i = categoryService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("category_id", i));
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("category-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = categoryService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @RequiresPermissions("category-list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination pagination = categoryService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @Override
    @PostMapping("/detail")
    @RequiresPermissions("category-detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = categoryService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/get-tree")
    @RequiresPermissions("category-get-tree")
    public GXResultUtils getTree(@RequestBody Dict param) {
        final List<Dict> list = categoryService.getTree(param);
        return GXResultUtils.ok().putData(list);
    }

    @PostMapping("/close")
    @RequiresPermissions("category-close")
    public GXResultUtils close(@RequestBody Dict param) {
        final boolean status = categoryService.closeStatus(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }

    @PostMapping("/open")
    @RequiresPermissions("category-open")
    public GXResultUtils open(@RequestBody Dict param) {
        final boolean status = categoryService.openStatus(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }

    @PostMapping("/freeze")
    @RequiresPermissions("category-freeze")
    public GXResultUtils freeze(@RequestBody Dict param) {
        final boolean status = categoryService.freezeStatus(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }
}
