package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.modules.system.entity.SAdminMenuEntity;

public interface SAdminMenuService extends GXBusinessService<SAdminMenuEntity>, GXValidateDBExists {
    /**
     * 开启
     *
     * @param param 参数
     * @return boolean
     */
    boolean openStatus(Dict param);

    /**
     * 关闭
     *
     * @param param 参数
     * @return boolean
     */
    boolean closeStatus(Dict param);

    /**
     * 冻结
     *
     * @param param 参数
     * @return boolean
     */
    boolean freezeStatus(Dict param);
}
