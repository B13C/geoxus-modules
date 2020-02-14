package com.geoxus.modules.message.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.message.service.MessageService;
import org.apache.ibatis.jdbc.SQL;

@SuppressWarnings("unused")
public class MessageBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_message");
        sql.WHERE(StrUtil.format("status = {}", GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_message");
        sql.WHERE(StrUtil.format("{} = {} AND status = {}", MessageService.PRIMARY_KEY, param.getInt(MessageService.PRIMARY_KEY), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "s_message";
    }
}
