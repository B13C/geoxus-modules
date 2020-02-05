package com.geoxus.modules.general.service;

import cn.hutool.core.lang.Dict;

import java.util.List;

public interface StatisticsService {
    /**
     * 首页数据统计
     * 更具项目需求去实现这个接口
     *
     * @return
     */
    List<Dict> getDataStatistics();
}
