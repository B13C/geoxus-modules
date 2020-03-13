package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.modules.system.constant.SAdminHasRolesConstants;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.constant.SRoleMenuConstants;
import org.apache.ibatis.jdbc.SQL;

public class SMenuBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SMenuConstants.TABLE_NAME);
        return sql.toString();
    }

    /**
     * 获取指定角色的菜单的权限列表
     *
     * <pre>
     *     {@code
     *     getRoleAllPerms(1,2,3,4)
     *     }
     * </pre>
     *
     * @param roles 参数 角色ID的字符串表示
     * @return String
     */
    public String getRoleAllPerms(String roles) {
        String sql = "select menu.menu_id , menu.perms from s_menu menu \n" +
                "INNER JOIN s_role_menu ON s_role_menu.menu_id = menu.menu_id\n" +
                "WHERE s_role_menu.role_id in ({})";
        sql = StrUtil.format(sql, roles);
        return sql;
    }

    /**
     * 获取指定管理员的菜单列表
     *
     * <pre>
     *     {@code
     *     getAdminAllPerms(2)
     *     }
     * </pre>
     *
     * @param adminId 管理员ID
     * @return String
     */
    public String getAdminAllPerms(Long adminId) {
        String sql = "SELECT menu.menu_id , menu.perms FROM s_menu menu  " +
                "INNER JOIN s_admin_menu ON s_admin_menu.menu_id = menu.menu_id " +
                "WHERE s_admin_menu.admin_id = {}";
        return StrUtil.format(sql, adminId);
    }

    /**
     * 获取用户的菜单列表
     *
     * @param adminId 管理员ID
     * @return String
     */
    public String getAllMenuId(Long adminId) {
        final SQL sql = new SQL().SELECT(StrUtil.format("{}.menu_id", SMenuConstants.TABLE_NAME))
                .FROM(SAdminHasRolesConstants.TABLE_NAME);
        sql.LEFT_OUTER_JOIN(StrUtil.format("{} on {}.role_id = {}.role_id",
                SRoleMenuConstants.TABLE_NAME,
                SAdminHasRolesConstants.TABLE_NAME,
                SRoleMenuConstants.TABLE_NAME));
        sql.LEFT_OUTER_JOIN(StrUtil.format("{} on {}.menu_id = {}.menu_id",
                SMenuConstants.TABLE_NAME,
                SMenuConstants.TABLE_NAME,
                SRoleMenuConstants.TABLE_NAME));
        sql.WHERE(StrUtil.format("{}.admin_id = {}", SAdminHasRolesConstants.TABLE_NAME, adminId));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return new SQL().toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return SMenuConstants.TABLE_NAME;
    }
}
