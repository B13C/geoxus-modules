package com.geoxus.modules.ethereum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

@Data
@TableName("u_wallet")
@ConditionalOnExpression(value = "${eth-enable:true}")
@EqualsAndHashCode(callSuper = false)
public class UWalletEntity extends GXBaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    private long walletId;

    private long userId;

    private String password;

    private String dynamicCode;

    private String walletFile;

    private String address;

    private int isPlatform = 0;

    private int status;
}
