package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.modules.system.entity.SCategoryEntity;

import java.util.List;

public interface SCategoryService extends GXBusinessService<SCategoryEntity>, GXValidateDBExists {
    /**
     * 获取树状结构
     *
     * @param param
     * @return
     */
    List<Dict> getTree(Dict param);

    /**
     * 开启
     *
     * @param param
     * @return
     */
    boolean openStatus(Dict param);

    /**
     * 关闭
     *
     * @param param
     * @return
     */
    boolean closeStatus(Dict param);

    /**
     * 冻结
     *
     * @param param
     * @return
     */
    boolean freezeStatus(Dict param);
}
