package com.geoxus.modules.general.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.service.GXApiIdempotentService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.framework.entity.GXCoreModelEntity;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.user.entity.UUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PostMapping("/get-all-models")
    public GXResultUtils getAllModels() {
        final ArrayList<Dict> data = CollUtil.newArrayList();
        final List<GXCoreModelEntity> list = coreModelService.list(new QueryWrapper<GXCoreModelEntity>().select("model_id", "model_show"));
        if (null != list && !list.isEmpty()) {
            for (GXCoreModelEntity entity : list) {
                final Dict set = Dict.create().set("model_id", entity.getModelId()).set("model_show", entity.getModelShow());
                data.add(set);
            }
        }
        return GXResultUtils.ok().putData(data);
    }
}
