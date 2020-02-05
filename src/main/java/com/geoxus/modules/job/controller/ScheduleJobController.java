package com.geoxus.modules.job.controller;

import com.geoxus.core.common.annotation.GXSysLogAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.validator.impl.GXValidatorUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.job.entity.ScheduleJobEntity;
import com.geoxus.modules.job.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务
 */
@RestController
@RequestMapping("/sys/schedule")
@ConditionalOnExpression("'${enable-quartz}'.equals('true')")
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 定时任务列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys-schedule-list")
    public GXResultUtils list(@RequestParam Map<String, Object> params) {
        GXPagination page = scheduleJobService.queryPage(params);
        return GXResultUtils.ok().putData(page);
    }

    /**
     * 定时任务信息
     */
    @RequestMapping("/info/{jobId}")
    @RequiresPermissions("sys-schedule-info")
    public GXResultUtils info(@PathVariable("jobId") Long jobId) {
        ScheduleJobEntity schedule = scheduleJobService.getById(jobId);

        return GXResultUtils.ok().putData(schedule);
    }

    /**
     * 保存定时任务
     */
    @GXSysLogAnnotation("保存定时任务")
    @RequestMapping("/save")
    @RequiresPermissions("sys-schedule-save")
    public GXResultUtils save(@RequestBody ScheduleJobEntity scheduleJob) {
        GXValidatorUtils.validateEntity(scheduleJob);
        scheduleJobService.saveJob(scheduleJob);
        return GXResultUtils.ok();
    }

    /**
     * 修改定时任务
     */
    @GXSysLogAnnotation("修改定时任务")
    @RequestMapping("/update")
    @RequiresPermissions("sys-schedule-update")
    public GXResultUtils update(@RequestBody ScheduleJobEntity scheduleJob) {
        GXValidatorUtils.validateEntity(scheduleJob);
        scheduleJobService.update(scheduleJob);
        return GXResultUtils.ok();
    }

    /**
     * 删除定时任务
     */
    @GXSysLogAnnotation("删除定时任务")
    @RequestMapping("/delete")
    @RequiresPermissions("sys-schedule-delete")
    public GXResultUtils delete(@RequestBody Long[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);
        return GXResultUtils.ok();
    }

    /**
     * 立即执行任务
     */
    @GXSysLogAnnotation("立即执行任务")
    @RequestMapping("/run")
    @RequiresPermissions("sys-schedule-run")
    public GXResultUtils run(@RequestBody Long[] jobIds) {
        scheduleJobService.run(jobIds);
        return GXResultUtils.ok();
    }

    /**
     * 暂停定时任务
     */
    @GXSysLogAnnotation("暂停定时任务")
    @RequestMapping("/pause")
    @RequiresPermissions("sys-schedule-pause")
    public GXResultUtils pause(@RequestBody Long[] jobIds) {
        scheduleJobService.pause(jobIds);
        return GXResultUtils.ok();
    }

    /**
     * 恢复定时任务
     */
    @GXSysLogAnnotation("恢复定时任务")
    @RequestMapping("/resume")
    @RequiresPermissions("sys-schedule-resume")
    public GXResultUtils resume(@RequestBody Long[] jobIds) {
        scheduleJobService.resume(jobIds);
        return GXResultUtils.ok();
    }
}
