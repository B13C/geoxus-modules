package com.geoxus.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.user.constant.UBalanceConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName(UBalanceConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class UBalanceEntity extends GXBaseEntity implements Serializable {
    @TableId
    private int balanceId;

    private long userId;

    private double balance;

    private int type;

    @Version
    private Integer version = 0;
}
