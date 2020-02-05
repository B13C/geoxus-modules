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
@TableName("s_message")
@EqualsAndHashCode(callSuper = false)
public class MessageEntity extends GXBaseEntity {
    @TableId
    private int messageId;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private String title;

    private int type;

    private String contents;

    private int templateId;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "s_message", fieldName = "ext")
    @NotNull
    private String ext = "{}";

    private int status;
}
