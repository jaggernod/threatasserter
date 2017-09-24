package com.jaggernod.threatasserter.internal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@SuppressWarnings({"unused", "ProhibitedExceptionDeclared"})
@Aspect
public class WorkerThreadAspect {

    private static final String TAG = WorkerThreadAspect.class.getSimpleName();

    private static final String POINTCUT_METHOD =
            "execution(@com.jaggernod.threatasserter.annotations.AssertWorkerThread * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.jaggernod.threatasserter.annotations.AssertWorkerThread *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methods() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructors() {
    }

    @Around("methods() || constructors()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Assertions.assertWorkerThread();

        return joinPoint.proceed();
    }
}
