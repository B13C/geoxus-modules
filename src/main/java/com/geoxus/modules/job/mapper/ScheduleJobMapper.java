package com.geoxus.modules.job.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.job.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 定时任务
 */
@Mapper
public interface ScheduleJobMapper extends GXBaseMapper<ScheduleJobEntity> {
    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);
}
