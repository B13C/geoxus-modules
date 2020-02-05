package com.geoxus.modules.contents.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
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
public class ContentController {
    @Autowired
    private ContentService contentService;

    @PostMapping("/add")
    @GXLoginAnnotation
    public GXResultUtils add(@Valid @GXRequestBodyToBeanAnnotation ContentEntity postsEntity) {
        contentService.save(postsEntity);
        return GXResultUtils.ok().putData(Dict.create().set("id", postsEntity.getContentId()));
    }

    @PostMapping("/modify-field")
    @GXLoginAnnotation
    public GXResultUtils modifyField(@RequestBody Dict param) {
        final ContentEntity contentEntity = contentService.getById(param.getLong(contentService.getPrimaryKey(false)));
        contentService.updateById(contentService.modifyEntityJSONFieldMultiValue(contentEntity, param));
        return GXResultUtils.ok().putData(param);
    }

    @PostMapping("/edit")
    @GXLoginAnnotation
    public GXResultUtils edit(@Valid @GXRequestBodyToBeanAnnotation ContentEntity postsEntity) {
        System.out.println(postsEntity);
        final boolean b = postsEntity.updateById();
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = contentService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }
}
