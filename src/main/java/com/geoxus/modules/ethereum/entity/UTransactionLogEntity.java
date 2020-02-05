package com.geoxus.modules.ethereum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.io.Serializable;

@Data
@TableName("u_transaction_log")
@EqualsAndHashCode(callSuper = false)
@ConditionalOnExpression(value = "${eth-enable:true}")
public class UTransactionLogEntity extends GXBaseEntity implements Serializable {
    private int id;

    private long userId;

    private int blockNumber;

    private int timestamp;

    private String transactionHash;

    private String ethFrom;

    private String ethTo;

    private String tokenAmount;

    private String ethAmount;

    private int status;

    private int type;

    private String txData;
}
