package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.modules.system.constant.SRoleMenuConstants;
import com.geoxus.modules.system.service.SMenuService;
import com.geoxus.modules.system.service.SRolesService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(SRoleMenuConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SRoleMenuEntity {
    @TableId
    private Integer id;

    @GXValidateDBExistsAnnotation(service = SMenuService.class, fieldName = "menu_id")
    private Integer menuId;

    @GXValidateDBExistsAnnotation(service = SRolesService.class, fieldName = "role_id")
    private Integer roleId;
}
