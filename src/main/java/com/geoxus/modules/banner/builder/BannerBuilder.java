package com.geoxus.modules.banner.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.banner.constant.BannerConstants;
import org.apache.ibatis.jdbc.SQL;

public class BannerBuilder implements GXBaseBuilder {
    @GXFieldCommentAnnotation(zh = "模型的值")
    String MODEL_IDENTIFICATION_VALUE = "s_banner";

    public String listOrSearch(Dict param) {
        SQL sql = new SQL().SELECT("*").FROM("s_banner");
        mergeSearchConditionToSQL(sql, param);
        sql.ORDER_BY("sort desc , created_at desc");
        return sql.toString();
    }

    public String detail(Dict param) {
        SQL sql = new SQL().SELECT("*").FROM("s_banner");
        sql.WHERE("banner_id = " + param.getInt(BannerConstants.PRIMARY_KEY));
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("intro", GXBaseBuilderConstants.BEFORE_LIKE)
                .set("status", GXBaseBuilderConstants.NUMBER_EQ)
                .set("type", GXBaseBuilderConstants.NUMBER_EQ)
                .set("province_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("city_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("user_id", GXBaseBuilderConstants.NUMBER_EQ)
                //.set("position", GXBaseBuilderConstants.NUMBER_EQ)
                .set("created_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return MODEL_IDENTIFICATION_VALUE;
    }
}
