package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.service.GXApiIdempotentService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.framework.entity.GXCoreModelEntity;
import com.geoxus.core.framework.service.GXCoreMediaLibraryService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.user.entity.UUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common/mobile")
public class CommonController {
    @Autowired
    private GXApiIdempotentService apiIdempotentService;

    @Autowired
    private GXCoreModelService coreModelService;

    @Autowired
    private GXCoreMediaLibraryService coreMediaLibraryService;

    @PostMapping("/get-api-idempotent-token")
    @GXLoginAnnotation
    public GXResultUtils getApiIdempotentToken(@GXLoginUserAnnotation UUserEntity uUserEntity) {
        return GXResultUtils.ok().putData(Dict.create().set("api-token", apiIdempotentService.createApiIdempotentToken(Dict.parse(uUserEntity))));
    }

    @GetMapping("/get-model-attribute")
    public GXResultUtils getModelAttribute(@RequestBody Dict param) {
        final Integer coreModelId = param.getInt("coreModelId");
        final String fieldName = param.getStr("fieldName");
        final GXCoreModelEntity modelEntity = coreModelService.getModelDetailByModelId(coreModelId, fieldName);
        return GXResultUtils.ok().putData(modelEntity);
    }
}
