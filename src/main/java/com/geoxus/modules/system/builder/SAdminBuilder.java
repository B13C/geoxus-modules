package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.constant.SAdminConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class SAdminBuilder implements GXBaseBuilder {
    public String listOrSearch(Dict param) {
        StringBuilder sql = new StringBuilder("select");
        sql.append(" s_admin.id as id,s_admin.nick_name as nickName,s_admin.username as username,s_admin.remark as remark,");
        sql.append(" s_admin.status as status,s_roles.id as roleId,s_roles.name as roleName,s_admin.created_at as created_at");
        sql.append(" from s_admin s_admin");
        sql.append(" LEFT JOIN s_admin_has_roles s_admin_has_roles on s_admin_has_roles.s_admin_id = s_admin.id");
        sql.append(" LEFT JOIN s_roles s_roles on s_roles.id = s_admin_has_roles.role_id");
        sql.append(" where s_admin.status <> 2");
        if (StringUtils.isNotBlank(param.getStr("nick_name"))) {
            sql.append(" and s_admin.nick_name like '%" + param.getStr("nick_name") + "%'");
        }
        if (StringUtils.isNotBlank(param.getStr("username"))) {
            sql.append(" and s_admin.username like '%" + param.getStr("username") + "%'");
        }
        if (param.getInt("status") != null && param.getInt("status") != GXBusinessStatusCode.DELETED.getCode()) {
            sql.append(" and s_admin.status = " + param.getInt("status"));
        }
        if (StringUtils.isNotBlank(param.getStr("start_at"))) {
            sql.append(" and s_admin.created_at >= " + param.getStr("start_at"));
        }
        if (StringUtils.isNotBlank(param.getStr("end_at"))) {
            sql.append(" and s_admin.created_at <= " + param.getStr("end_at"));
        }
        if (StringUtils.isNotBlank(param.getStr("sidx"))) {
            sql.append(" ORDER BY " + param.getStr("sidx"));
            sql.append(param.getStr("order") == null ? " asc" : " desc");
        } else {
            sql.append(" ORDER BY id asc");
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_admin");
        sql.WHERE(StrUtil.format("{} = {}", SAdminConstants.PRIMARY_KEY, param.getInt(SAdminConstants.PRIMARY_KEY)));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return SAdminConstants.TABLE_NAME;
    }
}
