package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色与管理员对应表
 */
@Data
@TableName("s_admin_has_roles")
@EqualsAndHashCode(callSuper = false)
public class SAdminHasRolesEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private int id;

    /**
     * 角色ID
     */
    private int roleId;

    /**
     * 管理员ID
     */
    private int adminId;

    /**
     * 模型类型(使用的模型,主要用于PHP)
     */
    private String modelType;

}
