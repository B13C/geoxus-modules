package com.geoxus.modules.contents.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.contents.constant.ContentConstants;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontendContentController")
@RequestMapping("/content/frontend")
public class ContentController implements GXController<ContentEntity> {
    @Autowired
    private ContentService contentService;

    @Override
    @PostMapping("/create")
    @GXLoginAnnotation
    public GXResultUtils create(@Validated(value = {GXCreateGroup.class}) @GXRequestBodyToEntityAnnotation ContentEntity target) {
        contentService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(ContentConstants.PRIMARY_KEY, target.getContentId()));
    }

    @PostMapping("/modify-field")
    @GXLoginAnnotation
    public GXResultUtils modifyField(@RequestBody Dict param) {
        contentService.updateFieldByCondition(ContentEntity.class, param, Dict.create().set(ContentConstants.PRIMARY_KEY, contentService.getPrimaryKey()));
        return GXResultUtils.ok().putData(param);
    }

    @Override
    @PostMapping("/update")
    @GXLoginAnnotation
    public GXResultUtils update(@Validated(value = {GXUpdateGroup.class}) @GXRequestBodyToEntityAnnotation() ContentEntity target) {
        final long contentId = contentService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(contentService.getPrimaryKey(), contentId));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination<Dict> pagination = contentService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = contentService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }
}
