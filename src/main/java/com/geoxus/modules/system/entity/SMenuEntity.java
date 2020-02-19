package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.modules.system.constant.SMenuConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(SMenuConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SMenuEntity extends GXBaseEntity {
    @TableId
    private int menuId;

    private int parentId;

    private String menuName;

    private String url;

    private String perms;

    private int type;

    private int icon;

    private int sort;

    private int status;
}
