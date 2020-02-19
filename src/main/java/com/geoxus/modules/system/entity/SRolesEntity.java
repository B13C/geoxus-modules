package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.system.constant.SRolesConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色表
 */
@Data
@TableName(SRolesConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SRolesEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId
    private int roleId;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 显示名字
     */
    private String showName;

    /**
     * 保护的类型(web,api,主要用于PHP)
     */
    private String guardName;

    /**
     * 角色的类型
     */
    private int type;

    /**
     * 状态
     */
    private int status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 层级(0-1-2)(用于特殊场合),比如:用户自己又有自己的分配权限
     */
    private String path;

    /**
     * 父级ID(用于特殊场合),比如:用户自己又有自己的分配权限
     */
    private int parentId;
}
