package com.geoxus.modules.contents.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.service.GXUUserService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.contents.constant.CommentConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@TableName("p_comment")
@EqualsAndHashCode(callSuper = false)
public class CommentEntity extends GXBaseEntity {
    @TableId
    private int commentId;

    private int parentId;

    private int targetModelId;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private Integer coreModelId;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private Integer targetCoreModelId;

    private String modelType;

    @GXValidateExtDataAnnotation(tableName = CommentConstants.TABLE_NAME, fieldName = "ext")
    private String ext = "{}";

    private String comments;

    @GXValidateDBExistsAnnotation(service = GXUUserService.class, fieldName = "user_id")
    private long userId;

    private String path;

    private int status;
}
