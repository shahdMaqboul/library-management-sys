package com.library.management.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

@Aspect
@Component
public class LoggingAspect {
    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // After Returning advice for successful method execution in controllers
    @AfterReturning(pointcut = "execution(* com.library.management.controllers.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature().getName() + " controller method executed!");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + "ms");
    }

    // After Returning advice for successful method execution in services
    @AfterReturning(pointcut = "execution(* com.library.management.services.*.*(..))", returning = "result")
    public void logAfterReturningService(JoinPoint joinPoint, Object result) {
        long startTime = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature().getName() + " service method executed. And returned: " + result);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + "ms");
    }

    // After Throwing advice for exceptions in services and controllers
    @AfterThrowing(pointcut = "execution(* com.library.management.services.*.*(..)) || execution(* com.library.management.controllers.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        long startTime = System.currentTimeMillis();
        System.out.println("Exception in " + joinPoint.getSignature().getName() + " method. Exception: " + exception.getMessage());
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + "ms");
    }

}
