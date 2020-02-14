package com.geoxus.modules.goods.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GoodsBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_goods");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_goods");
        sql.WHERE(StrUtil.format("goods_id = {}", param.getInt("goods_id")));
        return sql.toString();
    }

    public String basicInfo(Dict param) {
        final SQL sql = new SQL().SELECT("goods_id, core_model_id, name, price").FROM("o_goods");
        sql.WHERE(StrUtil.format("goods_id = {}", param.getInt("goods_id")));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "o_goods";
    }
}
