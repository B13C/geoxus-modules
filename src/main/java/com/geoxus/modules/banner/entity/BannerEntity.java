package com.geoxus.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.banner.constant.BannerConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@TableName(BannerConstants.TABLE_NAME)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BannerEntity extends GXBaseEntity {
    @TableId
    private int bannerId;

    private String intro;

    private String url;

    @GXValidateExtDataAnnotation(tableName = "s_banner")
    private String ext;

    @NotNull
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private int sort;

    private int type;

    private int status;

    private int position;

    private int provinceId;

    private int cityId;

    private int areaId;
}
