package com.geoxus.modules.general.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.general.constant.SlogConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@TableName(SlogConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SlogEntity extends GXBaseEntity {
    @TableId
    private int logId;

    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    @NotEmpty
    private int coreModelId;

    private long modelId;

    private String ext;

    private String source;

    private long userId;

    public SlogEntity() {
    }

    public SlogEntity(int coreModelId, long modelId, String ext, String source, long userId) {
        this.coreModelId = coreModelId;
        this.modelId = modelId;
        this.ext = ext;
    }
}
