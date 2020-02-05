package com.geoxus.modules.job.util;

import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.modules.job.entity.ScheduleJobEntity;
import com.geoxus.modules.job.entity.ScheduleJobLogEntity;
import com.geoxus.modules.job.service.ScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 定时任务
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap().get(ScheduleJobEntity.JOB_PARAM_KEY);
        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) GXSpringContextUtils.getBean("scheduleJobLogService");
        //数据库保存执行记录
        ScheduleJobLogEntity jobLog = new ScheduleJobLogEntity();
        jobLog.setJobId(scheduleJob.getJobId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setCreateTime(new Date());
        //任务开始时间
        long startTime = System.currentTimeMillis();
        try {
            log.debug("任务准备执行，任务ID： {}", scheduleJob.getJobId());
            Object target = GXSpringContextUtils.getBean(scheduleJob.getBeanName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, scheduleJob.getParams());

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes((int) times);
            //任务状态    0：成功    1：失败
            jobLog.setStatus(0);
            log.debug("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
        } catch (Exception e) {
            log.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes((int) times);
            //任务状态    0：成功    1：失败
            jobLog.setStatus(1);
            jobLog.setError(StrUtil.sub(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.save(jobLog);
        }
    }
}
