package com.geoxus.modules.general.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;

public class RegionBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    public String areaInfo(Dict param) {
        final ArrayList<Integer> list = new ArrayList<>();
        if (null != param.getInt("province_id")) {
            list.add(param.getInt("province_id"));
        }
        if (null != param.getInt("city_id")) {
            list.add(param.getInt("city_id"));
        }
        if (null != param.getInt("area_id")) {
            list.add(param.getInt("area_id"));
        }
        final SQL sql = new SQL().SELECT("name").FROM("s_region").WHERE(StrUtil.format("id in ({})", Joiner.on(",").join(list)));
        return sql.toString();
    }
}
