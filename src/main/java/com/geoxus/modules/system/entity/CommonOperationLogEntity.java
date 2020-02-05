package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统操作日志
 */
@Data
@TableName("s_common_operation_log")
@EqualsAndHashCode(callSuper = false)
public class CommonOperationLogEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private int commonLogId;

    private int coreModelId = 0;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private long execTime;

    /**
     * IP地址
     */
    private String ip;
}
