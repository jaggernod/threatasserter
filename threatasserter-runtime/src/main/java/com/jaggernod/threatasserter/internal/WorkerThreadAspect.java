package com.jaggernod.threatasserter.internal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import android.util.Log;

@SuppressWarnings({"unused", "ProhibitedExceptionDeclared"})
@Aspect
public class WorkerThreadAspect {

    private static final String TAG = WorkerThreadAspect.class.getSimpleName();

    private static final String POINTCUT_METHOD =
            "execution(@de.axelspringer.yana.internal.aop.AssertWorkerThread * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@e.axelspringer.yana.internal.aop.AssertWorkerThread *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithAssertWorkerThread() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedAssertWorkerThread() {
    }

    @Around("methodAnnotatedWithAssertWorkerThread() || constructorAnnotatedAssertWorkerThread()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Log.e(TAG, className + "::" + methodName + " code must not run on the Main thread.");

        Assertions.assertWorkerThread();

        return joinPoint.proceed();
    }
}
