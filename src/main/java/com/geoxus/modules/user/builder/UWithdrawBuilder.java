package com.geoxus.modules.user.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.oauth.GXTokenManager;
import org.apache.ibatis.jdbc.SQL;

public class UWithdrawBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("u_withdraw");
        if (null != param.getInt(GXTokenManager.USER_ID)) {
            sql.WHERE("user_id = " + param.getLong(GXTokenManager.USER_ID));
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("u_withdraw");
        sql.WHERE(StrUtil.format("withdraw_number = {}", param.getLong("withdraw_number")));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "u_withdraw";
    }
}
