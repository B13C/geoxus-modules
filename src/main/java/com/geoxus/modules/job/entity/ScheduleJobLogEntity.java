package com.geoxus.modules.job.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("schedule_job_log")
public class ScheduleJobLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @GXFieldCommentAnnotation(zh = "日志id")
    @TableId
    private Long logId;

    @GXFieldCommentAnnotation(zh = "任务id")
    private Long jobId;

    @GXFieldCommentAnnotation(zh = "spring bean名称")
    private String beanName;

    @GXFieldCommentAnnotation(zh = "参数")
    private String params;

    @GXFieldCommentAnnotation(zh = "任务状态    0：成功    1：失败")
    private Integer status;

    /**
     * 失败信息
     */
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    private Integer times;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
