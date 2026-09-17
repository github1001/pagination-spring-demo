package com.example.pagination.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class RequestResponseLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingAspect.class);
    private final ObjectMapper objectMapper;

    public RequestResponseLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.example.pagination.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        HttpServletRequest request = currentRequest();
        String method = request == null ? "N/A" : request.getMethod();
        String uri = request == null ? "N/A" : request.getRequestURI();
        String query = request == null ? null : request.getQueryString();

        log.info("REQUEST method={} uri={}{} handler={} payload={}",
                method, uri, query == null ? "" : "?" + query,
                joinPoint.getSignature().toShortString(), safeJson(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            log.info("RESPONSE method={} uri={} durationMs={} payload={}",
                    method, uri, System.currentTimeMillis() - start, safeJson(result));
            return result;
        } catch (Throwable throwable) {
            log.error("RESPONSE method={} uri={} durationMs={} error={}",
                    method, uri, System.currentTimeMillis() - start, throwable.getMessage());
            throw throwable;
        }
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    private String safeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException | RuntimeException e) {
            return Arrays.toString(value instanceof Object[] array ? array : new Object[]{value});
        }
    }
}
