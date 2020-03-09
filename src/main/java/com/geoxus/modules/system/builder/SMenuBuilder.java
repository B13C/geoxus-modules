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
        final SQL sql = new SQL().SELECT("*").FROM("s_menu");
        return sql.toString();
    }

    /**
     * 获取指定用户的菜单的权限列表
     *
     * @param adminId 管理员ID
     * @return String
     */
    public String getAllPerms(Long adminId) {
        final SQL sql = new SQL().SELECT("s_menu.*").FROM(SAdminHasRolesConstants.TABLE_NAME);
        sql.LEFT_OUTER_JOIN(StrUtil.format("{} on {}.role_id = {}.role_id",
                SRoleMenuConstants.TABLE_NAME,
                SAdminHasRolesConstants.TABLE_NAME,
                SRoleMenuConstants.TABLE_NAME));
        sql.LEFT_OUTER_JOIN(StrUtil.format("{} on {}.menu_id = {}.menu_id",
                SMenuConstants.TABLE_NAME,
                SRoleMenuConstants.TABLE_NAME,
                SMenuConstants.TABLE_NAME));
        sql.WHERE(StrUtil.format("{}.admin_id = {}", SAdminHasRolesConstants.TABLE_NAME, adminId));
        return sql.toString();
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
        return "s_menu";
    }
}
