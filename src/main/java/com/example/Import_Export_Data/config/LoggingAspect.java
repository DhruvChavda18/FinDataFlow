package com.example.Import_Export_Data.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.example.Import_Export_Data..*(..)) && !execution(* com.example.Import_Export_Data.config.LoggingAspect.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint == null) {
            return null;
        }

        // Generate unique trace ID for request tracking
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature != null ? signature.getMethod().getName() : "unknown";
        String className = joinPoint.getTarget() != null ? 
            joinPoint.getTarget().getClass().getSimpleName() : "unknown";
        
        // Log method entry with arguments
        logMethodEntry(joinPoint, signature, methodName, className);
        
        // Execute method and measure execution time
        Instant start = Instant.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            logException(methodName, className, e);
            throw e;
        } finally {
            // Log method exit with execution time and result
            logMethodExit(methodName, className, result, start);
            MDC.clear();
        }
    }

    private void logMethodEntry(ProceedingJoinPoint joinPoint, MethodSignature signature, 
                              String methodName, String className) {
        if (joinPoint == null || signature == null) {
            logger.debug("Entering method: {}.{}", className, methodName);
            return;
        }

        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        StringBuilder argsLog = new StringBuilder();
        
        if (args != null && parameterNames != null) {
            for (int i = 0; i < args.length; i++) {
                if (i < parameterNames.length) {
                    argsLog.append(parameterNames[i]).append("=");
                }
                try {
                    if (args[i] != null) {
                        argsLog.append(objectMapper.writeValueAsString(args[i]));
                    } else {
                        argsLog.append("null");
                    }
                } catch (Exception e) {
                    argsLog.append(args[i] != null ? args[i].toString() : "null");
                }
                if (i < args.length - 1) {
                    argsLog.append(", ");
                }
            }
        }
        
        logger.debug("Entering method: {}.{} with arguments: {}", className, methodName, argsLog);
    }

    private void logMethodExit(String methodName, String className, Object result, Instant start) {
        Duration executionTime = Duration.between(start, Instant.now());
        try {
            String responseJson = result != null ? 
                objectMapper.writeValueAsString(result) : "null";
            logger.debug("Exiting method: {}.{} with response: {} (execution time: {}ms)", 
                className, methodName, responseJson, executionTime.toMillis());
        } catch (Exception e) {
            logger.debug("Exiting method: {}.{} with response: {} (execution time: {}ms)", 
                className, methodName, result, executionTime.toMillis());
        }
    }

    private void logException(String methodName, String className, Exception e) {
        if (e != null) {
            logger.error("Exception in method: {}.{} - {}: {}", 
                className, methodName, e.getClass().getSimpleName(), e.getMessage(), e);
        } else {
            logger.error("Unknown exception in method: {}.{}", className, methodName);
        }
    }
} 