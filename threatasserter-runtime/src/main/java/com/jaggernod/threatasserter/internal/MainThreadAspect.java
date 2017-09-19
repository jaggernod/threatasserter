package com.jaggernod.threatasserter.internal;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import android.util.Log;

@SuppressWarnings({"unused", "ProhibitedExceptionDeclared"})
@Aspect
public class MainThreadAspect {

    private static final String TAG = MainThreadAspect.class.getSimpleName();

    private static final String POINTCUT_METHOD =
            "execution(@com.jaggernod.threatasserter.annotations.AssertMainThread * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.jaggernod.threatasserter.annotations.AssertMainThread *.new(..))";

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

        Log.e(TAG, className + "::" + methodName + " code must not run on the Main thread.");

        Assertions.assertMainThread();

        return joinPoint.proceed();
    }
}
