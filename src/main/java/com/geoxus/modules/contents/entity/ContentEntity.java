package com.geoxus.modules.contents.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.contents.service.ContentService;
import com.geoxus.modules.system.service.SCategoryService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName("p_content")
@EqualsAndHashCode(callSuper = false)
public class ContentEntity extends GXBaseEntity {
    @TableId
    @GXValidateDBExistsAnnotation(service = ContentService.class, fieldName = "content_id", groups = GXUpdateGroup.class)
    @NotNull(groups = {GXUpdateGroup.class})
    private int contentId;

    @GXValidateDBExistsAnnotation(service = SCategoryService.class, fieldName = "category_id")
    private int categoryId = 0;

    @NotNull(message = "core_model_id不能为空")
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private Integer coreModelId;

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    private String keywords;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "p_content", fieldName = "ext", groups = {GXUpdateGroup.class, GXCreateGroup.class})
    private String ext = "{}";

    private int status = 0;
}
