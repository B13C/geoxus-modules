package com.geoxus.modules.job.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXQueryUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.job.entity.ScheduleJobLogEntity;
import com.geoxus.modules.job.mapper.ScheduleJobLogMapper;
import com.geoxus.modules.job.service.ScheduleJobLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scheduleJobLogService")
@ConditionalOnExpression("'${enable-quartz}'.equals('true')")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLogEntity> implements ScheduleJobLogService {
    @Override
    public GXPagination queryPage(Map<String, Object> params) {
        String jobId = (String) params.get("jobId");

        IPage<ScheduleJobLogEntity> page = this.page(
                new GXQueryUtils<ScheduleJobLogEntity>().getPage(params),
                new QueryWrapper<ScheduleJobLogEntity>().like(StrUtil.isNotBlank(jobId), "job_id", jobId)
        );
        return new GXPagination(page);
    }
}
