package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色与权限的对应表
 */
@Data
@TableName("s_role_has_permissions")
@EqualsAndHashCode(callSuper = false)
public class SRoleHasPermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @GXFieldCommentAnnotation(zh = "权限ID")
    private int permissionId;

    @GXFieldCommentAnnotation(zh = "角色ID")
    private int roleId;

    @GXFieldCommentAnnotation(zh = "模型类型")
    private String modelType = "default";
}
