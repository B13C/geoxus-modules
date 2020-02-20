package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private long permissionId;

    /**
     * 管理员ID
     */
    private long adminId;

    /**
     * 模型类型(用户获取用户权限,主要用于PHP)
     */
    private String modelType;
}
