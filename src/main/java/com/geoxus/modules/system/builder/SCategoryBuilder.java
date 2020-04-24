package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.constant.SCategoryConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.Optional;

public class SCategoryBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SCategoryConstants.TABLE_NAME);
        addConditionToSearchCondition(param, "status", GXBusinessStatusCode.NORMAL.getCode());
        mergeSearchConditionToSQL(sql, param, SCategoryConstants.TABLE_NAME);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SCategoryConstants.TABLE_NAME);
        final Integer categoryId = Optional.ofNullable(param.getInt(SCategoryConstants.PRIMARY_KEY)).orElse(0);
        sql.WHERE(StrUtil.format("{} = {}", SCategoryConstants.PRIMARY_KEY, categoryId));
        return sql.toString();
    }

    public String getCategoryName(Dict param) {
        final SQL sql = new SQL().SELECT("category_name").FROM(SCategoryConstants.TABLE_NAME);
        final Integer categoryId = Optional.ofNullable(param.getInt(SCategoryConstants.PRIMARY_KEY)).orElse(0);
        sql.WHERE(StrUtil.format("{} = {}", SCategoryConstants.PRIMARY_KEY, categoryId));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(SCategoryConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set("status", GXBaseBuilderConstants.NUMBER_EQ)
                .set(GXCommonConstants.CORE_MODEL_PRIMARY_NAME, GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return SCategoryConstants.MODEL_IDENTIFICATION;
    }
}
