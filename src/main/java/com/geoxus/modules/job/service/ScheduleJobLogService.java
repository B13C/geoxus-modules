package com.geoxus.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {
    GXPagination queryPage(Map<String, Object> params);
}
