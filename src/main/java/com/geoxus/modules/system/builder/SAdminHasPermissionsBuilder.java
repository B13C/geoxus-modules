package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.system.constant.SAdminHasPermissionsConstants;
import org.apache.ibatis.jdbc.SQL;

public class SAdminHasPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("sp.permission_id,sp.permission_code,sp.show_name")
                .FROM("s_permissions sp");
        sql.INNER_JOIN("s_admin_has_permissions sahp ON sahp.permission_id =sp.permission_id");
        mergeSearchConditionToSQL(sql, param, "sahp");
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return "";
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create().set("admin_id", GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return SAdminHasPermissionsConstants.TABLE_NAME;
    }
}
