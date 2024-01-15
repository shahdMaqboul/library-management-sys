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

    // Before advice for method calls in controllers
    @Before("execution(* com.library.management.services.*.*(..)) || execution(* com.library.management.controllers.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + " controller method executed!");
    }

    // After Returning advice for successful method execution in services
    @AfterReturning(pointcut =
            "execution(* com.library.management.services.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + " service method executed. And returned: " + result);
    }

    // After Throwing advice for exceptions in services and controllers
    @AfterThrowing(pointcut = "execution(* com.library.management.services.*.*(..)) || execution(* com.library.management.controllers.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        System.out.println("Exception in " + joinPoint.getSignature().getName() + " method. Exception: " + exception.getMessage());
    }

}
