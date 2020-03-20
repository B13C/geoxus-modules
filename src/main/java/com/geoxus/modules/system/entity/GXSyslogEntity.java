package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.system.constant.GXSyslogConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统操作日志
 */
@Data
@TableName(GXSyslogConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class GXSyslogEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private int logId;

    @GXValidateDBExistsAnnotation
    private int coreModelId = 0;

    @GXFieldCommentAnnotation(zh = "用户名")
    private String username;

    @GXFieldCommentAnnotation(zh = "用户操作")
    private String operation;

    @GXFieldCommentAnnotation(zh = "请求方法")
    private String method;

    @GXFieldCommentAnnotation(zh = "请求参数")
    private String params;

    @GXFieldCommentAnnotation(zh = "执行时长(毫秒)")
    private long execTime;

    @GXFieldCommentAnnotation(zh = "IP地址")
    private String ip;
}
