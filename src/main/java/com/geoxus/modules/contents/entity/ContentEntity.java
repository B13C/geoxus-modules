package com.geoxus.modules.contents.entity;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@TableName("p_content")
@EqualsAndHashCode(callSuper = false)
public class ContentEntity extends GXBaseEntity {
    @TableId
    private int contentId;

    private int categoryId = 0;

    @Min(1)
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private String title;

    private String contents;

    private String keywords;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "p_content", fieldName = "ext")
    private String ext = "{}";

    private int status = 0;

    @TableField(exist = false)
    private List<Dict> comments;
}
