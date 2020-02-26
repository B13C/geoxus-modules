package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.util.GXResultUtils;
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
     * @return GXResultUtils
     */
    @PostMapping("/get-region-tree")
    public GXResultUtils getRegionTree(@RequestBody Dict param) {
        List<Dict> list = regionService.getRegionTree(param);
        return GXResultUtils.ok().putData(list);
    }
}
