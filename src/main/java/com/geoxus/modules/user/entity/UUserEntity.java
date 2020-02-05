package com.geoxus.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXUUserEntity;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@TableName("u_user")
@EqualsAndHashCode(callSuper = false)
public class UUserEntity extends GXUUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private long userId;

    private long parentId;

    private String username;

    private String password;

    private String salt;

    private String wxOpenId;

    private String qqOpenId;

    private String aliPayOpenId;

    private String payPassword;

    private String paySalt;

    private String nickName;

    private String phone;

    private String path;

    private int hierarchy;

    @GXValidateExtDataAnnotation(tableName = "u_user", fieldName = "ext")
    private String ext = "{}";

    private int status;

    @GXValidateDBExistsAnnotation(service = GXCoreModelService.class, fieldName = "model_id")
    @NotNull
    private int coreModelId;

    private int userType;

    private int lastLoginAt;

    private String inviteCode;
}
