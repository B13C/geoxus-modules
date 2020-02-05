package com.geoxus.modules.message.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.message.service.MessageService;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@SuppressWarnings("unused")
public class UserHasMessageBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("user_has_message");
        sql.WHERE(StrUtil.format("user_id= {} and status = {}", param.getInt(GXTokenManager.USER_ID), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("s_message");
        sql.WHERE(StrUtil.format("{} = {} AND status = {}", MessageService.PRIMARY_KEY, param.getInt(MessageService.PRIMARY_KEY), GXBusinessStatusCode.DELETED.getCode()));
        return sql.toString();
    }

    public String unReadMessage(Dict param) {
        final SQL sql = new SQL().SELECT("s_message.content,s_message.title,s_message.id,s_message.`status`,s_message.created_at as createdAt").FROM("s_message");
        sql.WHERE(StrUtil.format("NOT EXISTS (SELECT message_id from user_has_message where s_message.id=user_has_message.message_id and user_id = {} and s_message.status = {})", param.getInt(GXTokenManager.USER_ID), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    public String readMessage(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("user_has_message");
        sql.WHERE(StrUtil.format("user_id= {} and status = {}", param.getInt(GXTokenManager.USER_ID), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    public String batchDeleteByIds(List<Integer> list) {
        final String condition = list.toString().replace('[', '(').replace(']', ')');
        return new SQL().UPDATE("user_has_message").SET(StrUtil.format("status = {}", GXBusinessStatusCode.DELETED.getCode())).WHERE("id in " + condition).toString();
    }
}
