package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.system.entity.SRolesEntity;

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
}

