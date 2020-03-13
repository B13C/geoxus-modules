package com.geoxus.modules.system.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.system.constant.SAdminConstants;
import org.apache.ibatis.jdbc.SQL;

public class SAdminBuilder implements GXBaseBuilder {
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("sa.admin_id,sa.nick_name,sa.username,sa.remark,sa.`status`,sa.created_at,sr.role_id,sr.role_name");
        sql.FROM(StrUtil.format("{} as {}", SAdminConstants.TABLE_NAME, "sa"));
        sql.LEFT_OUTER_JOIN("s_admin_roles sahr ON sahr.admin_id = sa.admin_id");
        sql.LEFT_OUTER_JOIN("s_roles sr ON sr.role_id = sahr.role_id");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final String selectFieldStr = getSelectFieldStr(SAdminConstants.TABLE_NAME, CollUtil.newHashSet("password", "salt", "is_super_admin"), true);
        final SQL sql = new SQL().SELECT(selectFieldStr).FROM(SAdminConstants.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(SAdminConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set("username", GXBaseBuilderConstants.AFTER_LIKE)
                .set("sr.role_name", GXBaseBuilderConstants.AFTER_LIKE)
                .set("sa.status", GXBaseBuilderConstants.NUMBER_EQ)
                .set("sr.status", GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return SAdminConstants.MODEL_IDENTIFICATION_VALUE;
    }
}
