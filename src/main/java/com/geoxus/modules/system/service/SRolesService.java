package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.system.entity.SRolesEntity;

import java.util.List;

public interface SRolesService extends GXBusinessService<SRolesEntity> {
    /**
     * 冻结指定角色
     *
     * @param param 参数
     * @return boolean
     */
    boolean freeze(Dict param);

    /**
     * 解冻指定角色
     *
     * @param param 参数
     * @return boolean
     */
    boolean unfreeze(Dict param);

    /**
     * 根据用户ID获取角色id列表
     *
     * @param adminId 管理员ID
     * @return List
     */
    List<Integer> getRoleIdsByAdminId(Long adminId);
}

