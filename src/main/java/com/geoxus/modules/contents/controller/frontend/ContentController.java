package com.geoxus.modules.contents.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.contents.constant.ContentConstants;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("frontendContentController")
@RequestMapping("/content/frontend")
public class ContentController implements GXController<ContentEntity> {
    @Autowired
    private ContentService contentService;

    @Override
    @PostMapping("/create")
    @GXLoginAnnotation
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation ContentEntity target) {
        contentService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(ContentConstants.PRIMARY_KEY, target.getContentId()));
    }

    @PostMapping("/modify-field")
    @GXLoginAnnotation
    public GXResultUtils modifyField(@RequestBody Dict param) {
        final ContentEntity contentEntity = contentService.getById(param.getLong(contentService.getPrimaryKey()));
        contentService.updateById(contentService.modifyEntityJSONFieldMultiValue(contentEntity, param));
        return GXResultUtils.ok().putData(param);
    }

    @Override
    @PostMapping("/edit")
    @GXLoginAnnotation
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation ContentEntity target) {
        final long contentId = contentService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("content_id", contentId));
    }

    @Override
    @PostMapping("/list-or-search")
    @GXLoginAnnotation
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return null;
    }

    @Override
    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = contentService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }
}
