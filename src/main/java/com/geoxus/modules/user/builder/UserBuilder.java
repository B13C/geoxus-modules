package com.geoxus.modules.user.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.modules.user.constant.UUserConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class UserBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("u_user");
        if (null != param.getStr("username")) {
            sql.WHERE(StrUtil.format("username = '{}'", param.getStr("username")));
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("u_user").WHERE(StrUtil.format("{} = {}", UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY)));
        return sql.toString();
    }

    public String children(Dict param) {
        final SQL sql = new SQL().SELECT("id,parent_id,username,path,path_level,core_model_id").FROM("u_user");
        sql.WHERE("path_level <=" + param.getInt("level") + " AND FIND_IN_SET( '" + param.getLong("user_id") + "', path)");
        return sql.toString();
    }

    public String fansCount(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as count").FROM("u_user").WHERE(" FIND_IN_SET( '" + param.getLong("parent_id") + "', path)");
        return sql.toString();
    }

    public String countNumber(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as count").FROM("u_user");
        if (null != param.get("identity_type")) {
            sql.WHERE(StrUtil.format("identity_type = '{}'", param.get("identity_type")));
        }
        return sql.toString();
    }

    public String specialInfo(Dict param) {
        final SQL sql = new SQL().SELECT("core_model_id, id, username, nick_name, ext").FROM("u_user").WHERE(StrUtil.format("id = {}", param.getLong("user_id")));
        return sql.toString();
    }

    public String baseInfoDetail(Dict param, List<String> fields) {
        String defaultField = "id, username, nick_name";
        if (null != fields && !fields.isEmpty()) {
            defaultField = String.join(",", fields);
        }
        final SQL sql = new SQL().SELECT(StrUtil.format("{}", defaultField)).FROM("u_user").WHERE(StrUtil.format("id = {}", param.getLong("user_id")));
        return sql.toString();
    }
}
