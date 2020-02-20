package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import org.apache.ibatis.jdbc.SQL;

public class SPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    /**
     * 获取所有的权限码
     *
     * @return
     */
    public String getAllPermissionCode() {
        final SQL sql = new SQL().SELECT("permission_code").FROM(SPermissionsConstants.TABLE_NAME);
        return sql.toString();
    }

    /**
     * 通过参数获取用户的权限列表
     * 获取的权限列表包括:
     * 直接分配给用户的
     * 所属角色中包含的
     *
     * @param param 参数
     * @return String
     */
    public String getAdminAllPermissions(Dict param) {
        String sql = "select s_permissions.permission_code from s_permissions \n" +
                "INNER JOIN s_admin_has_permissions ON s_admin_has_permissions.permission_id = s_permissions.permission_id\n" +
                "WHERE s_admin_has_permissions.admin_id = {admin_id}";
        sql += "\nUNION\n";
        sql += "select s_permissions.permission_code from s_permissions \n" +
                "INNER JOIN s_role_has_permissions ON s_role_has_permissions.permission_id =  s_permissions.permission_id\n" +
                "INNER JOIN s_admin_has_roles ON s_admin_has_roles.role_id = s_role_has_permissions.role_id\n" +
                "WHERE s_admin_has_roles.admin_id = {admin_id}";
        return StrUtil.format(sql, param);
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return SPermissionsConstants.TABLE_NAME;
    }
}
