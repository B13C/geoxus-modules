package com.geoxus.modules.system.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXSysLogAnnotation;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXShiroUtils;
import com.geoxus.modules.system.entity.CommonOperationLogEntity;
import com.geoxus.modules.system.service.CommonOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class GXSysLogAspect {
    @Autowired
    private CommonOperationLogService operationLogService;

    @Around("@annotation(com.geoxus.core.common.annotation.GXSysLogAnnotation)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        try {
            this.saveOperationLog(point, time);
        } catch (Exception e) {
            log.error("系统操作日志报错", e);
        }
        return result;
    }

    private void saveOperationLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CommonOperationLogEntity operationLog = new CommonOperationLogEntity();
        GXSysLogAnnotation log = method.getAnnotation(GXSysLogAnnotation.class);
        if (log != null) {
            //注解上的描述
            operationLog.setOperation(log.value());
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        operationLog.setMethod(className + "::" + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            operationLog.setParams(JSONUtil.toJsonStr(args));
        } catch (Exception e) {
            GXSysLogAspect.log.error(e.getMessage(), e);
        }
        //设置IP地址
        operationLog.setIp(GXHttpContextUtils.getIP(GXHttpContextUtils.getHttpServletRequest()));
        //用户名
        String nickName = Optional.ofNullable(GXShiroUtils.getAdminData().getStr("user_name")).orElse(GXShiroUtils.getAdminData().getStr(StrUtil.toCamelCase("user_name")));
        operationLog.setUsername(nickName);
        operationLog.setExecTime(time);
        //保存系统日志
        operationLogService.save(operationLog);
    }
}
