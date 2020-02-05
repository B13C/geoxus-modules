package com.geoxus.modules.contents.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import org.apache.ibatis.jdbc.SQL;

public class ContentBuilder implements GXBaseBuilder {
    @Override
    public String detail(Dict param) {
        return new SQL().SELECT("*").FROM("p_content").WHERE("id = " + param.getInt("id")).toString();
    }

    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("p_content");
        mergeSearchConditionToSQL(sql, param);
        sql.WHERE(StrUtil.format("status = {}", GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }


    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("category_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("title", GXBaseBuilderConstants.AFTER_LIKE)
                .set("keywords", GXBaseBuilderConstants.AFTER_LIKE)
                .set("created_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ)
                .set("core_model_id", GXBaseBuilderConstants.NUMBER_EQ);
    }
}
