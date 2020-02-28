package com.geoxus.modules.contents.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.contents.constant.FeedBackConstants;
import com.geoxus.modules.user.constant.UUserConstants;
import org.apache.ibatis.jdbc.SQL;

public class FeedBackBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT(FeedBackConstants.TABLE_ALIAS_NAME + ".*," + UUserConstants.TABLE_ALIAS_NAME + ".username")
                .FROM(StrUtil.format("{} as {}", FeedBackConstants.TABLE_NAME, FeedBackConstants.TABLE_ALIAS_NAME))
                .LEFT_OUTER_JOIN("u_user " + UUserConstants.TABLE_ALIAS_NAME + " on " + UUserConstants.TABLE_ALIAS_NAME + ".user_id = " + FeedBackConstants.TABLE_ALIAS_NAME + ".user_id");
        mergeSearchConditionToSQL(sql, param, FeedBackConstants.TABLE_ALIAS_NAME);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(FeedBackConstants.TABLE_NAME).WHERE(FeedBackConstants.PRIMARY_KEY + " = " + param.getLong(FeedBackConstants.PRIMARY_KEY));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(StrUtil.format("{}.{}", FeedBackConstants.TABLE_ALIAS_NAME, "feedback_type"), GXBaseBuilderConstants.NUMBER_EQ)
                .set(StrUtil.format("{}.{}", FeedBackConstants.TABLE_ALIAS_NAME, "user_id"), GXBaseBuilderConstants.NUMBER_EQ)
                .set(StrUtil.format("{}.{}", FeedBackConstants.TABLE_ALIAS_NAME, "status"), GXBaseBuilderConstants.NUMBER_EQ)
                .set(StrUtil.format("{}.{}", UUserConstants.TABLE_ALIAS_NAME, "user_name"), GXBaseBuilderConstants.AFTER_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return FeedBackConstants.TABLE_NAME;
    }
}
