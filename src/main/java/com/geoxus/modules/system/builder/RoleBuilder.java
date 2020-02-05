package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.service.SRolesService;
import org.apache.ibatis.jdbc.SQL;

public class RoleBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_roles");
        if (null != param.getStr("username")) {
            sql.WHERE(StrUtil.format("username like {}%", param.getStr("username")));
        }
        sql.WHERE(StrUtil.format("status = {}", GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final int id = param.getInt(SRolesService.PRIMARY_KEY);
        final SQL sql = new SQL().SELECT("*").FROM("s_roles").WHERE(StrUtil.format("{} = {}", SRolesService.PRIMARY_KEY, id));
        return sql.toString();
    }
}
