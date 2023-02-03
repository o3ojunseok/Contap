package com.project.contap.common.aop;


import com.project.contap.common.util.AddLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class UseTimeAop {
    private Long setRunTime = 300L;
    @Around("execution(public * com.project.contap.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        long startTime = System.currentTimeMillis();
        try {
            Object output = joinPoint.proceed();
            return output;
        } finally {
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            if(runTime >= setRunTime)
                AddLog.addAPIUseTime(request,runTime);
        }
    }
}
