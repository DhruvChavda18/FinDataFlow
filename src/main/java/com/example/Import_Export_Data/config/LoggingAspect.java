package com.example.Import_Export_Data.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Controller logging (executed first)
    @Around("execution(* com.example.Import_Export_Data..controller..*(..))") 
    @Order(1)
    public Object logControllerLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLayer(joinPoint, "Controller");
    }
    
    // Service logging (executed after controller)
    @Around("execution(* com.example.Import_Export_Data..service..*(..))") 
    @Order(2)
    public Object logServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLayer(joinPoint, "Service");
    }
    // Common logging method for both layers
    private Object logLayer(ProceedingJoinPoint joinPoint, String layerType) throws Throwable {
        if (joinPoint == null) return null;

        // Ensure traceId is available
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logMethodEntry(joinPoint, signature, methodName, className, layerType);

        Instant start = Instant.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            logException(methodName, className, e);
            throw e;
        } finally {
            logMethodExit(methodName, className, result, start, layerType);
        }
    }

    private void logMethodEntry(ProceedingJoinPoint joinPoint, MethodSignature signature,
                              String methodName, String className, String layerType) {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        StringBuilder argsLog = new StringBuilder();

        if (args != null && parameterNames != null) {
            for (int i = 0; i < args.length; i++) {
                if (i < parameterNames.length) {
                    argsLog.append(parameterNames[i]).append("=");
                }
                try {
                    argsLog.append(args[i] != null ? objectMapper.writeValueAsString(args[i]) : "null");
                } catch (Exception e) {
                    argsLog.append(args[i] != null ? args[i].toString() : "null");
                }
                if (i < args.length - 1) {
                    argsLog.append(", ");
                }
            }
        }

        logger.info("[{}] ENTRY - {}.{}() - Args: {}", layerType, className, methodName, argsLog);
    }

    private void logMethodExit(String methodName, String className, Object result,
                             Instant start, String layerType) {
        Duration executionTime = Duration.between(start, Instant.now());
        try {
            String responseJson = result != null ? objectMapper.writeValueAsString(result) : "null";
            logger.info("[{}] EXIT - {}.{}() - Response: {} - Duration: {}ms",
                    layerType, className, methodName, responseJson, executionTime.toMillis());
        } catch (Exception e) {
            logger.info("[{}] EXIT - {}.{}() - Response: {} - Duration: {}ms",
                    layerType, className, methodName, result, executionTime.toMillis());
        }
    }

    private void logException(String methodName, String className, Exception e) {
        if (e != null) {
            errorLogger.error("Exception in method: {}.{} - {}: {}", className, methodName,
                    e.getClass().getSimpleName(), e.getMessage(), e);
        } else {
            errorLogger.error("Unknown exception in method: {}.{}", className, methodName);
        }
    }
}
