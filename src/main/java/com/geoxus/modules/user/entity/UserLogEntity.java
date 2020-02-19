package com.geoxus.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.user.constant.UserLogConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(UserLogConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class UserLogEntity extends GXBaseEntity {
    @TableId
    private int userLogId;

    private int coreModelId;

    private long userId;

    private String ext;

    private String eventName;

    private String action;

    private String ip;
}
