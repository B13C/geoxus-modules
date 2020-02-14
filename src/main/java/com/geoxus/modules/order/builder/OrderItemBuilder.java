package com.geoxus.modules.order.builder;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.order.constant.OrderConstants;
import com.geoxus.modules.order.service.OrderItemService;
import com.geoxus.modules.user.constant.UUserConstants;
import org.apache.ibatis.jdbc.SQL;

public class OrderItemBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_order_item");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("o_order_item");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    public String orderItemBasicInfo(Dict param) {
        final SQL sql = new SQL().SELECT("goods_id , quantity , consumer_code ,consumer_at,status").FROM("o_order_item");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(OrderItemService.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set(OrderConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set(UUserConstants.PRIMARY_KEY, GXBaseBuilderConstants.NUMBER_EQ)
                .set("status", GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return "o_order_item";
    }
}
