package com.geoxus.modules.contents.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("p_comment")
@EqualsAndHashCode(callSuper = false)
public class CommentEntity extends GXBaseEntity {
    @TableId
    private int commentId;

    private int parentId;

    private int modelId;

    private int coreModelId;

    private String modelType;

    @GXValidateExtDataAnnotation(tableName = "p_comment", fieldName = "ext")
    private String ext;

    private String comments;

    private long userId;

    private String path;

    private int status;
}
