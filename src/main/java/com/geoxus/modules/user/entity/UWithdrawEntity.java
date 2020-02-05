package com.geoxus.modules.user.entity;

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
@TableName("u_withdraw")
@EqualsAndHashCode(callSuper = false)
public class UWithdrawEntity extends GXBaseEntity {
    private int withdrawId;

    @TableId(type = IdType.INPUT)
    private long withdrawSn;

    private double amount;

    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    private int coreModelId;

    @GXValidateExtDataAnnotation(tableName = "u_withdraw", fieldName = "ext")
    private String ext;

    private int status;

    private long userId;

    private int completedAt;

    private int cancelAt;
}
