package com.geoxus.modules.general.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.modules.general.entity.SRegionEntity;

import java.util.List;

public interface SRegionService extends GXBaseService<SRegionEntity> {
    /**
     * 获取所有区域树
     *
     * @return
     */
    List<SRegionEntity> getRegionTree();

    /**
     * 通过条件获取区域
     *
     * @param param
     * @return
     */
    List<SRegionEntity> getRegion(Dict param);

    /**
     * 转换名字到拼音
     *
     * @return
     */
    boolean convertNameToPinYin();
}

