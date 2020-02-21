package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.service.SRolesService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色表
 */
@Data
@TableName(SRolesConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SRolesEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = -6001288064265545313L;

    @GXFieldCommentAnnotation(zh = "角色ID")
    @TableId
    @NotNull(groups = GXUpdateGroup.class)
    @GXValidateDBExistsAnnotation(groups = {GXUpdateGroup.class}, service = SRolesService.class, fieldName = SRolesConstants.PRIMARY_KEY)
    private Long roleId;

    @GXFieldCommentAnnotation(zh = "角色名字")
    private String roleName;

    @GXFieldCommentAnnotation(zh = "角色名字的拼音")
    private String rolePinyinName;

    @GXFieldCommentAnnotation(zh = "角色名字拼音的简写")
    private String rolePinyinShortName;

    @GXFieldCommentAnnotation(zh = "保护的类型(web,api,主要用于PHP)")
    private String guardName;

    @GXFieldCommentAnnotation(zh = "角色的类型")
    private int type;

    @GXFieldCommentAnnotation(zh = "状态")
    private int status;

    @GXFieldCommentAnnotation(zh = "备注")
    private String remark;

    @GXFieldCommentAnnotation(zh = "层级(0-1-2)(用于特殊场合),比如:用户自己又有自己的分配权限")
    private String path;

    @GXFieldCommentAnnotation(zh = "父级ID(用于特殊场合),比如:用户自己又有自己的分配权限")
    private Long parentId;
}
