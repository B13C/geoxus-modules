package com.geoxus.modules.contents.builder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.contents.constant.ContentConstants;
import org.apache.ibatis.jdbc.SQL;

public class ContentBuilder implements GXBaseBuilder {
    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(ContentConstants.TABLE_NAME);
        final int contentId = Convert.toInt(param.getInt(ContentConstants.PRIMARY_KEY), 0);
        sql.WHERE(StrUtil.format("{} = {}", ContentConstants.PRIMARY_KEY, contentId));
        return sql.toString();
    }

    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(ContentConstants.TABLE_NAME);
        addConditionToSearchCondition(param, "status", GXBusinessStatusCode.NORMAL.getCode());
        mergeSearchConditionToSQL(sql, param);
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

    @Override
    public String getModelIdentificationValue() {
        return ContentConstants.TABLE_NAME;
    }
}
