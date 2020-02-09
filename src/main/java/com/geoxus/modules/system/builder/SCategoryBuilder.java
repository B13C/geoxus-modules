package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.system.constant.SCategoryConstants;
import org.apache.ibatis.jdbc.SQL;

public class SCategoryBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_category");
        if (null != param.getInt("core_model_id")) {
            sql.WHERE(StrUtil.format("core_model_id = {}", param.getInt("core_model_id")));
        }
        sql.WHERE(StrUtil.format("status = {}", GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_category");
        sql.WHERE(StrUtil.format("id = {}", param.getInt(SCategoryConstants.PRIMARY_KEY)));
        return sql.toString();
    }
}
