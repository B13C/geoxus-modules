package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.general.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/general/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    /**
     * 首页数据统计
     * 根据业务的不同去创建service的实现类
     * 只有Service实现后才能调用,否则会报错
     *
     * @return
     */
    @PostMapping("/get-data-statistics")
    public GXResultUtils getDataStatistics() {
        List<Dict> list = statisticsService.getDataStatistics();
        return GXResultUtils.ok().putData(list);
    }
}
