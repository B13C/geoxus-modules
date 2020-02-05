package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXSAdminService;
import com.geoxus.modules.system.entity.SAdminEntity;

public interface SAdminService extends GXSAdminService<SAdminEntity> {
    /**
     * 修改密码
     *
     * @param param
     * @return
     */
    boolean changePassword(Dict param);

    /**
     * 登录
     *
     * @param param
     * @return
     */
    Dict login(Dict param);

    /**
     * 冻结账户
     *
     * @param param
     * @return
     */
    boolean freeze(Dict param);

    /**
     * 解冻账户
     *
     * @param param
     * @return
     */
    boolean unfreeze(Dict param);
}

