package com.geoxus.modules.general.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.modules.general.entity.SRegionEntity;

import java.util.List;

public interface SRegionService extends GXBaseService<SRegionEntity> {
    /**
     * 获取所有区域树
     *
     * @param param 参数
     * @return List
     */
    List<Dict> getRegionTree(Dict param);
}

