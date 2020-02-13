package com.geoxus.modules.user.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.user.constant.UUserConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashSet;
import java.util.List;

public class UserBuilder implements GXBaseBuilder {
    @GXFieldCommentAnnotation(zh = "模型的值")
    public static final String MODEL_IDENTIFICATION_VALUE = "u_user";

    @GXFieldCommentAnnotation(zh = "数据库表名")
    public static final String TABLE_NAME = "u_user";

    @Override
    public String listOrSearch(Dict param) {
        final HashSet<String> columns = new HashSet<>();
        columns.add("salt");
        columns.add("password");
        columns.add("pay_salt");
        columns.add("pay_password");
        final String selectColumns = getSelectFieldStr(TABLE_NAME, columns, true);
        final SQL sql = new SQL().SELECT(selectColumns).FROM("u_user");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final HashSet<String> columns = new HashSet<>();
        columns.add("salt");
        columns.add("password");
        columns.add("pay_salt");
        columns.add("pay_password");
        final String selectColumns = getSelectFieldStr(TABLE_NAME, columns, true);
        final SQL sql = new SQL().SELECT(selectColumns).FROM(TABLE_NAME).WHERE(StrUtil.format("{} = {}", UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY)));
        return sql.toString();
    }

    public String children(Dict param) {
        final SQL sql = new SQL().SELECT("id,parent_id,username,path,path_level,core_model_id").FROM("u_user");
        sql.WHERE("path_level <=" + param.getInt("level") + " AND FIND_IN_SET( '" + param.getLong("user_id") + "', path)");
        return sql.toString();
    }

    public String fansCount(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as count").FROM(TABLE_NAME).WHERE(" FIND_IN_SET( '" + param.getLong("parent_id") + "', path)");
        return sql.toString();
    }

    public String countNumber(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as count").FROM(TABLE_NAME);
        if (null != param.get("identity_type")) {
            sql.WHERE(StrUtil.format("identity_type = '{}'", param.get("identity_type")));
        }
        return sql.toString();
    }

    public String specialInfo(Dict param) {
        final SQL sql = new SQL().SELECT("core_model_id, id, username, nick_name, ext").FROM(TABLE_NAME).WHERE(StrUtil.format("id = {}", param.getLong("user_id")));
        return sql.toString();
    }

    public String baseInfoDetail(Dict param, List<String> fields) {
        String defaultField = "id, username, nick_name";
        if (null != fields && !fields.isEmpty()) {
            defaultField = String.join(",", fields);
        }
        final SQL sql = new SQL().SELECT(StrUtil.format("{}", defaultField)).FROM(TABLE_NAME).WHERE(StrUtil.format("id = {}", param.getLong("user_id")));
        return sql.toString();
    }

    @Override
    public String getModelIdentificationValue() {
        return MODEL_IDENTIFICATION_VALUE;
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create().set("name", GXBaseBuilderConstants.AFTER_LIKE);
    }
}
