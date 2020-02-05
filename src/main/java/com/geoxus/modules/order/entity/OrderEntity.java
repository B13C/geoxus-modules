package com.geoxus.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("o_orders")
@EqualsAndHashCode(callSuper = false)
public class OrderEntity extends GXBaseEntity {
    @TableId(type = IdType.AUTO)
    private int orderId;

    private long orderSn;

    private int orderType = 1;

    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    private long userId;

    private String username;

    private double orderPrice = 0.00;

    @GXValidateExtDataAnnotation(tableName = "o_orders", fieldName = "ext")
    private String ext = "{}";

    @GXValidateExtDataAnnotation(tableName = "o_orders", fieldName = "pay_info")
    private String payInfo = "{}";

    private int status;

    private long cancelAt = 0;

    private long completedAt = 0;
}
