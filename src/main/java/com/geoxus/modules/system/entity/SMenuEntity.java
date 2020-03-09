package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.service.SMenuService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(SMenuConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SMenuEntity extends GXBaseEntity {
    @TableId
    private int menuId;

    @GXValidateDBExistsAnnotation(service = SMenuService.class, fieldName = SMenuConstants.PRIMARY_KEY)
    private int parentId;

    private String menuName;

    private String url;

    private String perms;

    @GXFieldCommentAnnotation(zh = "类型 0 : 目录  1 : 菜单  2 : 按钮")
    private int type;

    private int icon;

    private int sort;

    private int status;
}
