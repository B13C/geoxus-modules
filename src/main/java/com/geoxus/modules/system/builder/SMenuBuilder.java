package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class SMenuBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_menu");
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return new SQL().toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "s_menu";
    }
}
