package com.geoxus.modules.contents.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.contents.constant.CommentConstants;
import org.apache.ibatis.jdbc.SQL;

public class CommentBuilder implements GXBaseBuilder {
    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("comment.* , user.username, user.nick_name , media.file_name as user_head").FROM(StrUtil.format("{} as comment", CommentConstants.TABLE_NAME));
        sql.INNER_JOIN("u_user user on user.user_id = comment.user_id");
        sql.INNER_JOIN("core_media_library media on media.model_id = user.user_id and media.core_model_id = user.core_model_id and media.resource_type='USER_HEAD'");
        sql.WHERE(StrUtil.format("id = {} and status = {}", param.getInt("id"), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return CommentConstants.TABLE_NAME;
    }

    @Override
    public String listOrSearch(Dict param) {
        final Dict searchCondition = getRequestSearchCondition(param);
        final SQL sql = new SQL().SELECT("comment.* , user.username, user.nick_name , media.file_name as user_head").FROM(StrUtil.format("{} as comment", CommentConstants.TABLE_NAME));
        sql.INNER_JOIN("u_user user on user.user_id = comment.user_id");
        sql.LEFT_OUTER_JOIN("core_media_library media on media.model_id = user.user_id and media.core_model_id = user.core_model_id and media.resource_type='USER_HEAD'");
        sql.WHERE(StrUtil.format("comment.model_id = {} and comment.core_model_id = {} and comment.status = {}", searchCondition.getInt("model_id"), searchCondition.getInt("core_model_id"), GXBusinessStatusCode.NORMAL.getCode()));
        return sql.toString();
    }
}
