package com.geoxus.modules.contents.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.modules.contents.constant.FeedBackConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.Set;

public class FeedBackBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("p_feedback.*,u_user.username,u_user.account_type").FROM("p_feedback")
                .LEFT_OUTER_JOIN("u_user on u_user.user_id = p_feedback.user_id");
        if (null != param.getInt("feedback_type")) {
            sql.WHERE(StrUtil.format("p_feedback.feedback_type = {}", param.getInt("feedback_type")));
        }
        if (null != param.getLong("user_id")) {
            sql.WHERE(StrUtil.format("p_feedback.user_id = {}", param.getLong("user_id")));
        }
        if (null != param.getInt("status")) {
            sql.WHERE(StrUtil.format("p_feedback.status = {}", param.getInt("status")));
        }
        if (null != param.getInt("created_at_begin")) {
            sql.WHERE(StrUtil.format("p_feedback.created_at >= {}", param.getInt("created_at_begin")));
        }
        if (null != param.getInt("created_at_end")) {
            sql.WHERE(StrUtil.format("p_feedback.created_at <= {}", param.getInt("created_at_end")));
        }
        if (null != param.getStr("user_name")) {
            sql.WHERE(StrUtil.format("u_user.username like '%{}%'", param.getStr("user_name")));
        }
        if (null != param.getInt("account_type")) {
            sql.WHERE(StrUtil.format("u_user.account_type = {}", param.getInt("account_type")));
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM("p_feedback").WHERE(FeedBackConstants.PRIMARY_KEY + " = " + param.getInt(FeedBackConstants.PRIMARY_KEY));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "p_feedback";
    }

    public String countByCondition(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as count").FROM("p_feedback");
        if (!param.isEmpty()) {
            final Set<String> keySet = param.keySet();
            for (String key : keySet) {
                sql.WHERE(StrUtil.format("{} = {}", key, param.get(key)));
            }
        }
        return sql.toString();
    }
}
