package com.geoxus.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXSAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员
 */
@Data
@TableName("s_admin")
@EqualsAndHashCode(callSuper = false)
public class SAdminEntity extends GXSAdminEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private int adminId;

    /**
     * 父级ID(用于特殊场景)
     */
    private int parentId;

    /**
     * 管理员昵称
     */
    private String nickName;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 盐(用于加密)
     */
    private String salt;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 额外数据
     */
    private String ext;
}
