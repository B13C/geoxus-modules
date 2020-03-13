package com.geoxus.modules.system.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import org.apache.ibatis.jdbc.SQL;

public class SPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    /**
     * 获取所有的权限码
     *
     * @return
     */
    public String getAllPermissionsCode() {
        final SQL sql = new SQL().SELECT("permission_code").FROM(SPermissionsConstants.TABLE_NAME);
        return sql.toString();
    }

    /**
     * 根据权限ID获取权限码
     *
     * @param permissionIds 权限码列表
     * @return Set<String>
     */
    public String getPermissionCodeByPermissionIds(String permissionIds) {
        final SQL sql = new SQL().SELECT("permission_code").FROM(SPermissionsConstants.TABLE_NAME);
        final String where = SPermissionsConstants.PRIMARY_KEY + " " + GXBaseBuilderConstants.IN;
        sql.WHERE(StrUtil.format(where, permissionIds));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return SPermissionsConstants.TABLE_NAME;
    }
}
