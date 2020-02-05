package com.geoxus.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.entity.CoreMediaLibraryEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@TableName(value = "o_goods")
@EqualsAndHashCode(callSuper = false)
public class GoodsEntity extends GXBaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private long goodsId;

    @Min(1)
    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    @NotNull
    private int coreModelId;

    private String goodsName;

    private double goodsPrice;

    private int sku;

    @Valid
    @GXValidateExtDataAnnotation(tableName = "goods", fieldName = "ext")
    @NotNull
    private String ext;

    private int status;

    @TableField(exist = false)
    private List<CoreMediaLibraryEntity> media;
}
