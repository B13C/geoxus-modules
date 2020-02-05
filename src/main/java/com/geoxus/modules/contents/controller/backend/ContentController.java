package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.service.ContentService;
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
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation() ContentEntity target) {
        contentService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", target.getContentId()));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation() ContentEntity target) {
        contentService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", target.getContentId()));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = contentService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination page = contentService.listOrSearch(param);
        return GXResultUtils.ok().putData(page);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = contentService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/update-part-field")
    public GXResultUtils updatePartField(@RequestBody Dict param) {
        final ContentEntity contentEntity = contentService.getById(param.getLong(contentService.getPrimaryKey(false)));
        contentService.updateById(contentService.modifyEntityJSONFieldMultiValue(contentEntity, param));
        return GXResultUtils.ok().putData(param);
    }
}
