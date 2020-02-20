package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXSPermissionsEntity;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 权限列表
 */
@Data
@TableName(SPermissionsConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SPermissionsEntity extends GXSPermissionsEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private int permissionId;

    @GXValidateDBExistsAnnotation
    private int coreModelId;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 权限保护类型(web,php，主要用于PHP)
     */
    private String guardName;

    /**
     * 权限名称
     */
    private String showName;

    /**
     * 父级ID
     */
    private int parentId;

    /**
     * 当前层级(1-2-3)
     */
    private String path;

    /**
     * 状态，1：正常，0：冻结
     */
    private int status;

    /**
     * 权限在当前模块下的顺序，由小到大
     */
    private int sort;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<SPermissionsEntity> children;
}
