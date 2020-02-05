package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员拥有的权限
 */
@Data
@TableName("s_admin_has_permissions")
@EqualsAndHashCode(callSuper = false)
public class SAdminHasPermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @TableId
    private int permissionId;

    /**
     * 管理员ID
     */
    private int adminId;

    /**
     * 模型类型(用户获取用户权限,主要用于PHP)
     */
    private String modelType;
}
