package com.geoxus.modules.contents.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.contents.constant.CommentConstants;
import org.apache.ibatis.jdbc.SQL;

public class CommentBuilder implements GXBaseBuilder {
    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("comment.* , user.username, user.nick_name , media.file_name as user_head").FROM(StrUtil.format("{} as comment", CommentConstants.TABLE_NAME));
        sql.INNER_JOIN("u_user user on user.user_id = comment.user_id");
        sql.LEFT_OUTER_JOIN("core_media_library media on media.object_id = user.user_id and media.core_model_id = user.core_model_id and media.resource_type='USER_HEAD'");
        mergeSearchConditionToSQL(sql, param, "comment");
        return sql.toString();
    }

    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("comment.* , user.username, user.nick_name , media.file_name as user_head").FROM(StrUtil.format("{} as comment", CommentConstants.TABLE_NAME));
        sql.INNER_JOIN("u_user user on user.user_id = comment.user_id");
        sql.LEFT_OUTER_JOIN("core_media_library media on media.object_id = user.user_id and media.core_model_id = user.core_model_id and media.resource_type='USER_HEAD'");
        mergeSearchConditionToSQL(sql, param, "");
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set(StrUtil.format("comment.{}", CommentConstants.PRIMARY_KEY), GXBaseBuilderConstants.NUMBER_EQ)
                .set("comment.object_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("comment.target_core_model_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("comment.status", GXBaseBuilderConstants.NUMBER_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return CommentConstants.TABLE_NAME;
    }
}
