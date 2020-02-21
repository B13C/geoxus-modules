package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBUniqueAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.entity.GXSAdminEntity;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.service.SAdminService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员
 */
@Data
@TableName(SAdminConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SAdminEntity extends GXSAdminEntity {
    private static final long serialVersionUID = -3041768205977915304L;

    @GXFieldCommentAnnotation(zh = "主键ID")
    @GXValidateDBExistsAnnotation(groups = {GXUpdateGroup.class}, service = SAdminService.class, fieldName = SAdminConstants.PRIMARY_KEY)
    @TableId
    private int adminId;

    @GXValidateDBExistsAnnotation()
    private int coreModelId;

    @GXValidateDBExistsAnnotation(groups = {GXUpdateGroup.class, GXCreateGroup.class}, service = SAdminService.class, fieldName = SAdminConstants.PRIMARY_KEY)
    @GXFieldCommentAnnotation(zh = "父级ID(用于特殊场景)")
    private int parentId;

    @GXFieldCommentAnnotation(zh = "管理员昵称")
    private String nickName;

    @GXFieldCommentAnnotation(zh = "用户登录名称")
    @GXValidateDBUniqueAnnotation(message = "用户名已经存在", service = SAdminService.class, fieldName = "username")
    private String username;

    @GXFieldCommentAnnotation(zh = "盐(用于加密)")
    private String salt;

    @GXFieldCommentAnnotation(zh = "加密后的密码")
    private String password;

    @GXFieldCommentAnnotation(zh = "手机号")
    private String phone;

    @GXFieldCommentAnnotation(zh = "邮箱")
    private String email;

    @GXFieldCommentAnnotation(zh = "备注")
    private String remark;

    @GXFieldCommentAnnotation(zh = "额外数据")
    @GXValidateExtDataAnnotation(groups = {GXCreateGroup.class, GXUpdateGroup.class}, tableName = SAdminConstants.TABLE_NAME)
    private String ext;
}
