package com.geoxus.modules.general.builder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

import java.util.Set;

public class SlogBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_log");
        if (null != param.getInt("user_id")) {
            sql.WHERE(StrUtil.format("user_id = {}", param.getInt("user_id")));
        }
        if (null != param.get("search_condition")) {
            final Dict searchCondition = Convert.convert(Dict.class, param.getObj("search_condition"));
            final Set<String> keySet = searchCondition.keySet();
            for (String key : keySet) {
                sql.WHERE(StrUtil.format("{} = {}", key, searchCondition.get(key)));
            }
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_log").WHERE(StrUtil.format("{} = {}", "id", param.getInt("id")));
        return sql.toString();
    }
}
