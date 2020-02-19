package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.constant.SCategoryConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.Optional;

public class SCategoryBuilder implements GXBaseBuilder {
    @GXFieldCommentAnnotation(zh = "表名")
    private static final String TABLE_NAME = "s_category";

    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(TABLE_NAME);
        addConditionToSearchCondition(param, "status", GXBusinessStatusCode.NORMAL.getCode());
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(TABLE_NAME);
        final Integer categoryId = Optional.ofNullable(param.getInt(SCategoryConstants.PRIMARY_KEY)).orElse(0);
        sql.WHERE(StrUtil.format("{} = {}", SCategoryConstants.PRIMARY_KEY, categoryId));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(SCategoryConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set("status", GXBaseBuilderConstants.NUMBER_EQ)
                .set("core_model_id", GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return TABLE_NAME;
    }
}
