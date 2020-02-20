package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.system.constant.SRoleHasPermissionsConstants;
import org.apache.ibatis.jdbc.SQL;

public class SRoleHasPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("sp.permission_id , sp.permission_code, sp.show_name")
                .FROM("s_permissions sp");
        sql.INNER_JOIN("s_role_has_permissions srhp ON srhp.permission_id =  sp.permission_id");
        sql.INNER_JOIN("s_admin_has_roles sahr ON sahr.role_id = srhp.role_id");
        mergeSearchConditionToSQL(sql, param, "sahr");
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("role_id", GXBaseBuilderConstants.IN);
    }

    @Override
    public String getModelIdentificationValue() {
        return SRoleHasPermissionsConstants.TABLE_NAME;
    }
}
