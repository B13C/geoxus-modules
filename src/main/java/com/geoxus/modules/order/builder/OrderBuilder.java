package com.geoxus.modules.order.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.order.constant.OrderConstants;
import org.apache.ibatis.jdbc.SQL;

public class OrderBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_orders");
        mergeSearchConditionToSQL(sql, param, "");
        sql.WHERE(StrUtil.format("status !={}", GXBusinessStatusCode.DELETED.getCode()));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_orders").WHERE(StrUtil.format("order_sn = {}", param.getLong(OrderConstants.PRIMARY_KEY)));
        if (null != param.getInt("user_id")) {
            sql.WHERE(StrUtil.format("user_id = {}", param.getLong("user_id")));
        }
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("user_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("username", GXBaseBuilderConstants.AFTER_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return "o_orders";
    }
}
