package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.general.entity.SRegionEntity;
import com.geoxus.modules.general.service.SRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/general/region")
public class RegionController {
    @Autowired
    private SRegionService regionService;

    /**
     * 获取所有区域树
     *
     * @return
     */
    @PostMapping("/get-region-tree")
    public GXResultUtils getRegionTree() {
        List<SRegionEntity> list = regionService.getRegionTree();
        return GXResultUtils.ok().putData(list);
    }

    /**
     * 通过条件获取区域
     *
     * @param param
     * @return
     */
    @PostMapping("/get-region")
    public GXResultUtils getRegion(@RequestBody Dict param) {
        List<SRegionEntity> list = regionService.getRegion(param);
        return GXResultUtils.ok().putData(list);
    }
}
