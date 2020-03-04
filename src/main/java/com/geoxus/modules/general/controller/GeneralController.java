package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.service.GXApiIdempotentService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.user.entity.UUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/general")
public class GeneralController {
    @Autowired
    private GXApiIdempotentService apiIdempotentService;

    @Autowired
    private GXCoreModelService coreModelService;

    @Autowired
    private GXCoreModelAttributesService gxCoreModelAttributesService;

    @PostMapping("/get-api-idempotent-token")
    @GXLoginAnnotation
    public GXResultUtils getApiIdempotentToken(@GXLoginUserAnnotation UUserEntity uUserEntity) {
        return GXResultUtils.ok().putData(Dict.create().set("api-token", apiIdempotentService.createApiIdempotentToken(Dict.parse(uUserEntity))));
    }

    @GetMapping("/get-model-attributes")
    public GXResultUtils getModelAttributes(@RequestBody Dict param) {
        final List<Dict> attributes = gxCoreModelAttributesService.getModelAttributesByModelId(param);
        return GXResultUtils.ok().putData(attributes);
    }
}
