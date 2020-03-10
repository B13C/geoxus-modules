package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBUniqueAnnotation;
import com.geoxus.core.common.entity.GXSPermissionsEntity;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import com.geoxus.modules.system.service.SPermissionsService;
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

    @GXFieldCommentAnnotation(zh = "主键ID")
    @TableId
    private int permissionId;

    private int coreModelId;

    @GXFieldCommentAnnotation(zh = "权限码")
    @GXValidateDBUniqueAnnotation(service = SPermissionsService.class, fieldName = "permission_code")
    private String permissionCode;

    @GXFieldCommentAnnotation(zh = "权限名称")
    private String showName;

    @GXFieldCommentAnnotation(zh = "归类(分类)")
    private int category;

    @GXFieldCommentAnnotation(zh = "状态，1：正常，0：冻结")
    private int status;

    @GXFieldCommentAnnotation(zh = "备注")
    private String remark;

    @TableField(exist = false)
    private List<SPermissionsEntity> children;
}
