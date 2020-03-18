package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXSAdminRolesEntity;
import com.geoxus.modules.system.constant.SAdminRolesConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色与管理员对应表
 */
@Data
@TableName(SAdminRolesConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SAdminRolesEntity extends GXSAdminRolesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private int id;

    /**
     * 角色ID
     */
    private long roleId;

    /**
     * 管理员ID
     */
    private long adminId;

    /**
     * 模型类型(使用的模型,主要用于PHP)
     */
    private String modelType;

}
