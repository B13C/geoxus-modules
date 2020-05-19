package com.geoxus.modules.core.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendGXCoreAttributesController")
@RequestMapping("/gx-core-attributes/frontend")
public class GXCoreAttributesController implements GXController<GXCoreAttributesEntity> {
    @Autowired
    private GXCoreAttributesService gxCoreAttributesService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation(validateCoreModelId = false) GXCoreAttributesEntity target) {
        long attributeId = gxCoreAttributesService.create(target, Dict.create());
        return GXResultUtils.ok().addKeyValue("attribute_id", attributeId);
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation(validateCoreModelId = false) GXCoreAttributesEntity target) {
        long attributeId = gxCoreAttributesService.update(target, Dict.create());
        return GXResultUtils.ok().addKeyValue("attribute_id", attributeId);
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        GXPagination<Dict> pagination = gxCoreAttributesService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils delete(@RequestBody Dict param) {
        Dict detail = gxCoreAttributesService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }
}
