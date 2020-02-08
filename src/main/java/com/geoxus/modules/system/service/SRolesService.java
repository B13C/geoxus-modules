package com.geoxus.modules.system.service;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.system.entity.SRolesEntity;

public interface SRolesService extends GXBusinessService<SRolesEntity> {
    @GXFieldCommentAnnotation(zh = "主键ID")
    String PRIMARY_KEY = "role_id";
}

