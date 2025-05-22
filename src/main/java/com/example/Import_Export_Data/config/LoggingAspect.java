package com.example.Import_Export_Data.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.Import_Export_Data..*.*(..))")
    public Object logMethodEntryExit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.debug("Entering method [{}] in class [{}]", methodName, className);
        
        try {
            Object result = joinPoint.proceed();
            logger.debug("Exiting method [{}] in class [{}]", methodName, className);
            return result;
        } catch (Exception e) {
            logger.error("Exception in method [{}] in class [{}]: {}", methodName, className, e.getMessage());
            throw e;
        }
    }
} 