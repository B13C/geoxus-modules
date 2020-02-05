package com.geoxus.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@TableName("s_banner")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BannerEntity extends GXBaseEntity {
    @TableId
    private int bannerId;

    //@ValidateExtDataAnnotation(tableName = "s_banner", fieldName = "ext")
    private String ext;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private int sort;

    private int type;

    private int status;

    private int position;
}
