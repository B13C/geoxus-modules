package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.constant.SAdminHasRolesConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import org.apache.ibatis.jdbc.SQL;

public class SRolesBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SRolesConstants.TABLE_NAME);
        addConditionToSearchCondition(param, "status", GXBusinessStatusCode.NORMAL.getCode());
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SRolesConstants.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    public String getRolesByAdminId(Long adminId) {
        SQL sql = new SQL().SELECT("s_roles.role_id,s_roles.role_name").FROM(SRolesConstants.TABLE_NAME);
        sql.INNER_JOIN("s_admin_has_roles ON s_admin_has_roles.role_id = s_roles.role_id");
        sql.WHERE("s_admin_has_roles.admin_id=#{adminId}");
        return sql.toString();
    }

    public String getIDS(Long adminId) {
        String sql = "SELECT role_id from {}  WHERE admin_id = {}";
        return StrUtil.format(sql, SAdminHasRolesConstants.TABLE_NAME, adminId);
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("role_name", GXBaseBuilderConstants.AFTER_LIKE)
                .set(SRolesConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return SRolesConstants.TABLE_NAME;
    }
}
