package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.contents.constant.ContentConstants;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.service.ContentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendContentController")
@RequestMapping("/content/backend")
public class ContentController implements GXController<ContentEntity> {
    @Autowired
    private ContentService contentService;

    @Override
    @PostMapping("/create")
    //@RequiresPermissions("content-create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation(groups = {GXCreateGroup.class}) ContentEntity target) {
        contentService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(ContentConstants.PRIMARY_KEY, target.getContentId()));
    }

    @Override
    @PostMapping("/update")
    @RequiresPermissions("content-update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation(groups = {GXUpdateGroup.class}) ContentEntity target) {
        contentService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(ContentConstants.PRIMARY_KEY, target.getContentId()));
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("content-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = contentService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @RequiresPermissions("content-list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination page = contentService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(page);
    }

    @Override
    @PostMapping("/detail")
    @RequiresPermissions("content-detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = contentService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/update-part-field")
    public GXResultUtils updatePartField(@RequestBody Dict param) {
        final ContentEntity contentEntity = contentService.getById(param.getLong(contentService.getPrimaryKey()));
        //contentService.updateById(contentService.modifyEntityJSONFieldMultiValue(contentEntity, param));
        return GXResultUtils.ok().putData(param);
    }
}
