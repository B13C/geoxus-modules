package com.geoxus.modules.system.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SCategoryEntity;
import com.geoxus.modules.system.service.SCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("frontendCategoryController")
@RequestMapping("/category/frontend")
public class CategoryController implements GXController<SCategoryEntity> {
    @Autowired
    private SCategoryService categoryService;

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination pagination = categoryService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = categoryService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/get-tree")
    public GXResultUtils getTree(@RequestBody Dict param) {
        final List<Dict> list = categoryService.getTree(param);
        return GXResultUtils.ok().putData(list);
    }
}
