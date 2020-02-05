package com.geoxus.modules.message.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@TableName("user_has_message")
@EqualsAndHashCode(callSuper = false)
public class UserHasMessageEntity extends GXBaseEntity {
    @TableId
    private int id;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private long userId;

    private int messageId;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "user_has_message", fieldName = "ext")
    @NotNull
    private String ext = "{}";

    private int status;
}
