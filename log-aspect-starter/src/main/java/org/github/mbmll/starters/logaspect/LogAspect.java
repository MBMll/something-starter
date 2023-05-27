package org.github.mbmll.starters.logaspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/27 14:34
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 将接口上的 LogPointcut 注解作为参数直接传递给使用了该 point cut 的通知方法(比如，下面的 around 通知)，
     * 从而避免通过反射去获取这个 LogPointcut 注解。注意名称变量须上下一致。
     */
    @Pointcut("@annotation(logPointcut)")
    public void methodLog(LogPointcut logPointcut) {
        /* this is a pointcut */
    }

    /**
     * typeLog.
     */
    @Pointcut("@within(org.github.mbmll.starters.logaspect.LogPointcut)")
    public void typeLog() {
        /* this is a pointcut */
    }

    /**
     * doMethodLogBefore.
     *
     * @param joinPoint joinPoint
     */
    @Before("methodLog(logPointcut)")
    public void doMethodLogBefore(JoinPoint joinPoint, LogPointcut logPointcut) {
        doTypeLogBefore(joinPoint);
    }

    /**
     * doMethodLogAfterThrowing.
     *
     * @param joinPoint joinPoint
     */
    @AfterThrowing("methodLog(logPointcut)")
    public void doMethodLogAfterThrowing(JoinPoint joinPoint, LogPointcut logPointcut) {
        doTypeLogAfterThrowing(joinPoint);
    }

    /**
     * doMethodLogAfterReturning.
     *
     * @param joinPoint joinPoint
     * @param obj       obj
     */
    @AfterReturning(returning = "obj", pointcut = "methodLog(logPointcut)")
    public void doMethodLogAfterReturning(JoinPoint joinPoint, LogPointcut logPointcut, Object obj) {
        doTypeLogAfterReturning(joinPoint, obj);
    }

    /**
     * doTypeLogBefore.
     *
     * @param joinPoint joinPoint
     */
    @Before("typeLog()")
    public void doTypeLogBefore(JoinPoint joinPoint) {
        log.info("start do--{} ，param--{}", LogFormatUtil.getInfo(joinPoint, true));
    }

    /**
     * doTypeLogAfterThrowing.
     *
     * @param joinPoint joinPoint
     */
    @AfterThrowing("typeLog()")
    public void doTypeLogAfterThrowing(JoinPoint joinPoint) {
        log.error("throw exception do--{}", LogFormatUtil.getInfo(joinPoint, false));
    }

    /**
     * doTypeLogAfterReturning.
     *
     * @param joinPoint joinPoint
     * @param obj       obj
     */
    @AfterReturning(returning = "obj", pointcut = "typeLog()")
    public void doTypeLogAfterReturning(JoinPoint joinPoint, Object obj) {
        StringBuilder result = new StringBuilder();
        log.info("end do--{}， return result --{}", LogFormatUtil.getInfo(joinPoint, false)[0], obj);
    }
}
