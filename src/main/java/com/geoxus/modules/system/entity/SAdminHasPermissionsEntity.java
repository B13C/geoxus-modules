package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.modules.system.constant.SAdminHasPermissionsConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员拥有的权限
 */
@Data
@TableName(SAdminHasPermissionsConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SAdminHasPermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private int id;

    @GXFieldCommentAnnotation(zh = "权限ID")
    private long permissionId;

    @GXFieldCommentAnnotation(zh = "管理员ID")
    private long adminId;
}
