package com.geoxus.modules.general.service;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.general.entity.SlogEntity;

import java.util.List;

public interface SlogService extends GXBusinessService<SlogEntity> {
    /**
     * 通过开始结束时间来获取记录
     *
     * @param userId  用户ID
     * @param startAt 开始时间
     * @param endAt   结束时间
     * @return
     */
    List<SlogEntity> getLogsByStartTimeAndEndTime(int userId, long startAt, long endAt);
}
