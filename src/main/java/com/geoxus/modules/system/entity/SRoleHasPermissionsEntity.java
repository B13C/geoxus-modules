package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.modules.system.constant.SRoleHasPermissionsConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色与权限的对应表
 */
@Data
@TableName(SRoleHasPermissionsConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SRoleHasPermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private long id;

    @GXFieldCommentAnnotation(zh = "权限ID")
    private long permissionId;

    @GXFieldCommentAnnotation(zh = "角色ID")
    private long roleId;
}
