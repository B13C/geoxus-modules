package com.geoxus.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("o_order_item")
@EqualsAndHashCode(callSuper = false)
public class OrderItemEntity extends GXBaseEntity {
    @TableId
    private int orderItemId;

    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private long orderSn;

    private long goodsId;

    private long userId;

    private int quantity;

    private double goodsPrice;

    private double purchasingPrice;

    @GXValidateExtDataAnnotation(tableName = "o_orders", fieldName = "ext")
    private String ext;

    private int status;
}
