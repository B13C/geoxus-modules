package com.geoxus.modules.general.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.general.constant.SRegionConstants;
import org.apache.ibatis.jdbc.SQL;

public class RegionBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(SRegionConstants.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(SRegionConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set("name", GXBaseBuilderConstants.AFTER_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return SRegionConstants.TABLE_NAME;
    }

    public String getNameByCondition(Dict param) {
        final SQL sql = new SQL().SELECT("name").FROM(SRegionConstants.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }
}
