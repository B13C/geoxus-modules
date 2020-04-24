package com.geoxus.modules.contents.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.modules.contents.constant.ContentConstants;
import com.geoxus.modules.contents.service.ContentService;
import com.geoxus.modules.system.service.SCategoryService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName(ContentConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class ContentEntity extends GXBaseEntity {
    @TableId
    @GXValidateDBExistsAnnotation(service = ContentService.class, fieldName = ContentConstants.PRIMARY_KEY, groups = GXUpdateGroup.class)
    @NotNull(groups = {GXUpdateGroup.class})
    private int contentId;

    @GXValidateDBExistsAnnotation(service = SCategoryService.class, fieldName = ContentConstants.PRIMARY_KEY)
    private int categoryId = 0;

    @NotNull(message = "core_model_id不能为空")
    @GXValidateDBExistsAnnotation()
    private Integer coreModelId;

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章标题")
    private String title;

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章内容")
    private String contents;

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章关键词")
    private String keywords;

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章描述")
    private String description;

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章来源")
    private String origin = "本站整理";

    @NotBlank
    @GXFieldCommentAnnotation(zh = "文章摘要")
    private String summary;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "p_content", groups = {GXUpdateGroup.class, GXCreateGroup.class})
    private String ext = "{}";

    private int status = 0;
}
