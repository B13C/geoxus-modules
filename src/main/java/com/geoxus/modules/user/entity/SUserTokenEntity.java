package com.geoxus.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.user.constant.SUserTokenConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户Token
 */
@Data
@TableName(SUserTokenConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SUserTokenEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    private long userId;

    /**
     * 用户Token
     */
    private String token;

    /**
     * 类型(1、admin  2、user)
     */
    private int type;

    /**
     * 过期时间
     */
    private int expiredAt;
}
